package com.rule.validator.formvalidator.validatableformitem

import android.view.View
import com.rule.validator.formvalidator.Validator
import com.google.android.material.textfield.TextInputLayout
import java.lang.ref.WeakReference

open class TextInputLayoutValidatableFormItem constructor(textInputLayout: TextInputLayout, validator: Validator, style: ValidationStyle)
    : BaseEditTextValidatableFormItem(textInputLayout.editText ?: error("Edit text can't be null"), validator, style) {

    private var weakTextInputLayout = WeakReference(textInputLayout)

    private val textInputLayout: TextInputLayout?
        get() = weakTextInputLayout.get()

    override fun onFurtherInit() {
        super.onFurtherInit()

        if (style == ValidationStyle.ON_FOCUS_LOST) {
            editText?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (!validator.checkValidation(editText?.text.toString())) {
                        showError()
                    } else {
                        clearError()
                    }
                }
            }
        }
    }

    override fun showError() {
        textInputLayout?.apply {
            error = validator.getErrorMessage()
            isErrorEnabled = true
        }
    }

    override fun clearError() {
        textInputLayout?.apply {
            error = null
            isErrorEnabled = false
        }
    }
}