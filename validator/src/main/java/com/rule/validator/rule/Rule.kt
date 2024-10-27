package com.rule.validator.rule

interface Rule {
    fun validate(text: String): Boolean

    val errorString: String

    val ignoreShowError: Boolean
        get() = false
}