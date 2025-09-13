package com.android.template.ui.login.reset.viewmodel

import com.android.template.R
import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.helpers.SECOND
import javax.inject.Inject

class ResetConfirmationCodeViewModel @Inject constructor(private val loginManager: LoginManager) :
    BaseViewModel() {

    fun confirmSecureCode(email: String, code: String, callback: () -> Unit) {
        if (code.length >= SECURE_CODE_SIZE) {
            makeRx(loginManager.sendResetPasswordCode(email, code)) {
                callback.invoke()
            }
        } else {
            showMessage(R.string.invalid_value)
        }
    }

    fun resendCode(email: String, callback: () -> Unit) {
        makeRx(loginManager.requestResetPasswordCode(email), callback)
    }

    companion object {
        private const val SECURE_CODE_SIZE = 6

        const val SECURE_CODE_ATTEMPT_DURATION = 31 * SECOND
    }
}