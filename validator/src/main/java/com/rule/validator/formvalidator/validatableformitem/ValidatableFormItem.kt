package com.rule.validator.formvalidator.validatableformitem

import io.reactivex.subjects.BehaviorSubject

interface ValidatableFormItem {
    fun showError()

    fun clearError()

    val isValid: Boolean

    var state: ValidatorState

    val onValueChangedObserver: BehaviorSubject<Boolean>

    fun validate(): Boolean

    fun onCleared() {}
}