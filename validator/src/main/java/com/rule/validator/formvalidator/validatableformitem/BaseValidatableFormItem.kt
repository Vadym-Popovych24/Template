package com.rule.validator.formvalidator.validatableformitem

import io.reactivex.subjects.BehaviorSubject

abstract class BaseValidatableFormItem : ValidatableFormItem {
    private var isValidValue: Boolean? = null

    override var state: ValidatorState = ValidatorState.ON

    override var isValid: Boolean
        get() = isValidValue ?: validate()
        set(value) {
            isValidValue = value
        }

    override val onValueChangedObserver = BehaviorSubject.createDefault(true)

   protected fun valueChangedDoValidation() {
        isValid = validate()

        onValueChangedObserver.onNext(isValid)

        if (state == ValidatorState.OFF) return

        if (!isValid)
            showError()
        else
            clearError()
    }
}