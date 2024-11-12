package com.android.template.ui.login.reset.viewmodel

import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.rule.validator.formvalidator.FormValidator
import javax.inject.Inject

class ResetPasswordViewModel @Inject constructor(private val loginManager: LoginManager) :
    BaseViewModel() {

    var actionCallback: (() -> Unit)? = null
    var resetPasswordFinishedCallback: (() -> Unit)? = null

    val passwordFormValidator = FormValidator(null)

    override fun onCleared() {
        super.onCleared()
        passwordFormValidator.clear()
    }

    fun saveNewPassword(email: String, code: String, password: String) {
            /*makeRx(
                loginManager.resetPassword(
                    email.getValueOrEmpty(),
                    code.getValueOrEmpty(),
                    password.getValueOrEmpty()
                )
            ) {*/
                resetPasswordFinishedCallback?.invoke()
       // }
    }

    override fun handleError(it: Throwable) {
        super.handleError(it)
        showMessage(it.localizedMessage?.toString() ?: "")
    }

}