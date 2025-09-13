package com.rule.validator.formvalidator.validatableformitem

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.rule.validator.formvalidator.Validator
import java.lang.ref.WeakReference

abstract class BaseEditTextValidatableFormItem constructor(inputEditText: EditText, protected val validator: Validator, protected val style: ValidationStyle) : BaseValidatableFormItem() {
    private var weakEditText: WeakReference<EditText> = WeakReference(inputEditText)

    protected val editText: EditText?
        get() = weakEditText.get()

    override var state: ValidatorState = ValidatorState.ON

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
            isValid = validate()

            onValueChangedObserver.onNext(isValid)

            if (state == ValidatorState.OFF) return

            if (style == ValidationStyle.ON_VALUE_CHANGED && !isValid)
                showError()
            else
                clearError()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    init {
        Handler(Looper.getMainLooper()).postDelayed({
            editText?.addTextChangedListener(textWatcher)
            onFurtherInit()
        }, 100)
    }

    override fun validate() = validator.checkValidation(editText?.text.toString())

    open fun onFurtherInit() {}

    override fun onCleared() {
        super.onCleared()
        editText?.removeTextChangedListener(textWatcher)
        clearError()
    }
}

enum class ValidatorState {
    ON, OFF
}



