package com.rule.validator.formvalidator

import android.os.Handler
import com.rule.validator.formvalidator.validatableformitem.ValidatableFormItem
import com.rule.validator.formvalidator.validatableformitem.ValidatorState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class FormValidator constructor(private var onValidate:((isValid: Boolean) -> Unit)?) {

    private val compositeDisposable = CompositeDisposable()
    private val validators = arrayListOf<ValidatableFormItem>()

    fun registerValidator(validatableFormItem: ValidatableFormItem) {
        validators.add(validatableFormItem)

        validatableFormItem.onValueChangedObserver
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                onValidate?.invoke(validateAllFields())
            }.addTo(compositeDisposable)
    }

    fun validateAllFields(): Boolean {
        return validators.all { formItem -> formItem.isValid }
    }

    fun getValidatorsCount(): Int {
        return validators.count()
    }

    fun preValidateAllFields(): Boolean {

        val resultValidations = mutableListOf<Boolean>()
        validators.forEach { validator ->
            val isValid = validator.validate()

            resultValidations.add(isValid)

            if (!isValid)
                validator.showError()
        }

        return resultValidations.all { it }
    }

    fun hideValidationErrorForAllFields() {
        validators.forEach { validator -> validator.clearError() }
    }

    fun runAllValidators() {
        onValidate?.invoke(validateAllFields())
    }

    fun doNotValidableAction(action: () -> Unit) {
        switch(ValidatorState.OFF)

        action.invoke()

        Handler().postDelayed({ switch(ValidatorState.ON) }, 300)
    }

    private fun switch(switchState: ValidatorState) {
        validators.forEach { validatableFormItem -> validatableFormItem.state = switchState }
    }

    val isFormValid
        get() = validateAllFields()

    fun clear() {
        compositeDisposable.clear()
        validators.apply {
            forEach { it.onCleared() }
            clear()
        }
    }
}


