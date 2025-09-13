package com.rule.validator.rule

import androidx.databinding.ObservableField
import java.math.BigDecimal
import java.text.ParseException

internal object ValidationRuleProvider {

    private const val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"

    class MaxLengthRule constructor(private val maxLength: Int, errorMessage: String) : Rule {
        override fun validate(text: String): Boolean = text.length <= maxLength

        override val errorString: String = errorMessage
    }

    class MinLengthRule constructor(private val minLength: Int, errorMessage: String) : Rule {
        override fun validate(text: String): Boolean = text.length >= minLength

        override val errorString: String = errorMessage
    }

    class MinMaxLengthRule constructor(private val minLength: Int, private val maxLength: Int, errorMessage: String) :
        Rule {
        override fun validate(text: String): Boolean = text.length in minLength..maxLength

        override val errorString: String = errorMessage
    }

    open class RegexRule(private val pattern: String, errorMessage: String) : Rule {
        override fun validate(text: String): Boolean = Regex(pattern).containsMatchIn(text)

        override val errorString: String = errorMessage
    }

    class RequiredRule(errorMessage: String, private val isRequired: Boolean) : Rule {
        override fun validate(text: String): Boolean = if (isRequired) !text.isBlank() else true

        override val errorString: String = errorMessage
    }

    class EmailRule(errorMessage: String) : RegexRule(emailRegex, errorMessage)

    class UserNameRule(errorMessage: String) : Rule {
        private val allowedSign = arrayListOf('-', '\'')

        override fun validate(text: String): Boolean {
            return text.toCharArray().all { char -> char.isLetter() || char.isWhitespace() || allowedSign.any { sing -> sing == char } }
        }

        override val errorString: String = errorMessage
    }

    open class StreetNameRule(private val errorMessage: String) : Rule {
        private val allowedSign = arrayListOf('-', '\'', ',', '.')
        private var lastIteratedCharacter: Char = Char.MIN_VALUE

        override fun validate(text: String): Boolean {
            val isValid = text.toCharArray().all { char ->
                lastIteratedCharacter = char
                char.isLetter() || char.isWhitespace() || char.isDigit() || allowedSign.any { sing -> sing == char }
            }

            if (isValid)
                lastIteratedCharacter = Char.MIN_VALUE

            return isValid
        }

        override val errorString: String
            get() = String.format(errorMessage, lastIteratedCharacter)
    }

    class CityRule(errorMessage: String) : StreetNameRule(errorMessage)

    class MinBigDecimalRule(private val minValue: BigDecimal, errorMessage: String) : Rule {
        override fun validate(text: String): Boolean {
            val value = text.toDecimal

            return value.let { it >= minValue }
        }

        override var errorString = errorMessage
    }


    class MaxBigDecimalRule(private val maxValue: BigDecimal, errorMessage: String) : Rule {
        override fun validate(text: String): Boolean {
            val value = text.toDecimal

            return value.let { it <= maxValue }
        }

        override var errorString = errorMessage
    }

    class MoreThanObservableDecimalRule(private val minValue: ObservableField<BigDecimal?>, errorMessage: String) :
        Rule {
        override fun validate(text: String): Boolean {
            val value = text.toDecimal

            if (minValue.get() == null) return true

            return value.let { it >= minValue.get()!! }
        }

        override var errorString = errorMessage
    }

    class MoreThanRule(private val minValue: BigDecimal, errorMessage: String, private val ignoreShowErrorParam: Boolean) :
        Rule {
        override fun validate(text: String): Boolean {
            val value = text.toDecimal

            return value > minValue
        }

        override var errorString = errorMessage

        override val ignoreShowError: Boolean
            get() = ignoreShowErrorParam
    }


    class LessThanDecimalRule(private val maxValue: ObservableField<BigDecimal?>, errorMessage: String) :
        Rule {
        override fun validate(text: String): Boolean {
            val value = text.toDecimal

            if (maxValue.get() == null) return true

            return value.let { it <= maxValue.get()!! }
        }

        override var errorString = errorMessage
    }

    class CustomRule(private val customFunction: () -> Boolean, errorMessage: String) : Rule {
        override fun validate(text: String): Boolean {

            return customFunction.invoke()
        }

        override var errorString = errorMessage
    }
}

val String?.toDecimal: BigDecimal
    get() {
        this ?: return BigDecimal.ZERO

        return try {
            BigDecimal("$this")
        } catch (ex: ParseException) {
            throw ex
        }
    }