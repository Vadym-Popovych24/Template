package com.rule.validator.formvalidator.validatableformitem

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.rule.validator.formvalidator.Validator
import java.lang.ref.WeakReference

open class EditTextValidatableFormItem constructor(inputEditText: EditText, validator: Validator, style: ValidationStyle, errorLabel: TextView? = null)
    : BaseEditTextValidatableFormItem(inputEditText, validator, style) {

    private var weakErrorLabel = WeakReference<TextView>(errorLabel)

    private val errorLabel: TextView?
        get() = weakErrorLabel.get()

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
        errorLabel?.let {
            it.text = validator.getErrorMessage()
            it.visibility = View.VISIBLE
        }?: run {
            editText?.setError(validator.getErrorMessage())
        }
    }

    override fun clearError() {
        errorLabel?.let {
            it.visibility = View.INVISIBLE
            it.text = ""
        }?: run {
            editText?.setError(null)
        }
    }
}