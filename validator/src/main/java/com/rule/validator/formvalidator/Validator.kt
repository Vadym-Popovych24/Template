package com.rule.validator.formvalidator

import androidx.annotation.IntRange
import androidx.databinding.ObservableField
import com.rule.validator.rule.Rule
import com.rule.validator.rule.ValidationRuleProvider
import java.math.BigDecimal
import kotlin.collections.ArrayList

class Validator constructor(private val builder: Builder) {

    private var errorMessage = ""

    fun checkValidation(text: String): Boolean {
        errorMessage = ""
        if (!builder.isRequiredField && text.isBlank()) return true

        var isComponentValid = true

        for (validationRule in builder.getValidationRules) {
            isComponentValid = validationRule.validate(text)
            if (!isComponentValid) {
                errorMessage = validationRule.errorString
                ignoreShowError = validationRule.ignoreShowError
                break
            }
        }

        return isComponentValid
    }

    fun getErrorMessage(): String = errorMessage

    var ignoreShowError: Boolean = false
        private set

    class Builder {
        internal var isRequiredField: Boolean = false

        private val collectionValidationRules = ArrayList<Rule>()

        fun maxLength(@IntRange(from = 1) maxLength: Int, errorString: String = "Please enter a value of $maxLength or less") = apply { collectionValidationRules.add(
            ValidationRuleProvider.MaxLengthRule(maxLength, errorString)) }

        fun minLength(@IntRange(from = 0) minLength: Int, errorString: String = "Please enter $minLength or more character") = apply { collectionValidationRules.add(
            ValidationRuleProvider.MinLengthRule(minLength, errorString)) }

        fun minMaxLength(@IntRange(from = 0) minLength: Int, maxLength: Int, errorString: String = "Please enter a value between $minLength and $maxLength") = apply { collectionValidationRules.add(
            ValidationRuleProvider.MinMaxLengthRule(minLength, maxLength, errorString)) }

        fun regexRule(pattern: String, errorString: String="") = apply { collectionValidationRules.add(
            ValidationRuleProvider.RegexRule(pattern, errorString)) }

        fun emailRule(errorString: String) = apply { collectionValidationRules.add(
            ValidationRuleProvider.EmailRule(errorString)) }

        fun userNameRule(errorString: String) = apply { collectionValidationRules.add(
            ValidationRuleProvider.UserNameRule(errorString)) }

        fun streetNameRule(errorString: String) = apply { collectionValidationRules.add(
            ValidationRuleProvider.StreetNameRule(errorString)) }

        fun cityRule(errorString: String) = apply { collectionValidationRules.add(
            ValidationRuleProvider.CityRule(errorString)) }

        fun minBigDecimalRule(minValue: BigDecimal, errorString: String) = apply { collectionValidationRules.add(
            ValidationRuleProvider.MinBigDecimalRule(minValue, errorString)) }

        fun maxBigDecimalRule(maxValue: BigDecimal, errorString: String) = apply { collectionValidationRules.add(
            ValidationRuleProvider.MaxBigDecimalRule(maxValue, errorString)) }

        fun maxAvailableBalanceRule(maxAvailableBalance: BigDecimal, errorString: String) = apply { collectionValidationRules.add(
            ValidationRuleProvider.MaxBigDecimalRule(maxAvailableBalance, errorString)) }

        fun requireRule(errorString: String, isRequired: Boolean = true) = apply {
            isRequiredField = isRequired
            collectionValidationRules.add(ValidationRuleProvider.RequiredRule(errorString, isRequired))
        }

        fun customRule(isRequired: Boolean, errorString: String = "", function: () -> Boolean) = apply {
            isRequiredField = isRequired
            collectionValidationRules.add(ValidationRuleProvider.CustomRule(function, errorString))
        }

        fun moreThanObservableValueRule(minValue: ObservableField<BigDecimal?>, errorString: String) = apply { collectionValidationRules.add(
            ValidationRuleProvider.MoreThanObservableDecimalRule(minValue, errorString)) }

        fun requireMoreThanZeroRule(errorString: String = "", ignoreShowError: Boolean = true) = apply { collectionValidationRules.add(
            ValidationRuleProvider.MoreThanRule(BigDecimal.ZERO, errorString, ignoreShowError)) }

        fun lessThanRule(maxValue: ObservableField<BigDecimal?>, errorString: String) = apply { collectionValidationRules.add(
            ValidationRuleProvider.LessThanDecimalRule(maxValue, errorString)) }

        fun build() = Validator(this)

        internal val getValidationRules
            get() = collectionValidationRules
    }
}