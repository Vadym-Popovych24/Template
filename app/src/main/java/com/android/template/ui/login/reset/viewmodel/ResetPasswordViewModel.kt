package com.android.template.ui.login.reset.viewmodel

import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.rule.validator.formvalidator.FormValidator
import javax.inject.Inject

class ResetPasswordViewModel @Inject constructor(private val loginManager: LoginManager) :
    BaseViewModel() {

    val passwordFormValidator = FormValidator()

    override fun onCleared() {
        super.onCleared()
        passwordFormValidator.clear()
    }

    fun saveNewPassword(email: String, code: String, password: String, callback: () -> Unit) {
        makeRx(
            loginManager.resetPassword(
                email = email,
                code = code,
                password = password
            ), callback
        )
    }

    fun saveFCMToken(fcmToken: String, completeCallback: (() -> Unit)? = null) {
        makeRxInvisible(loginManager.saveFCMToken(fcmToken), completeCallback)
    }

    override fun handleError(it: Throwable) {
        super.handleError(it)
        showMessage(it.localizedMessage?.toString() ?: "")
    }

}