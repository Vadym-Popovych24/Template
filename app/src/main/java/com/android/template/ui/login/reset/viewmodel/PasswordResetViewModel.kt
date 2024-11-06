package com.android.template.ui.login.reset.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.android.template.R
import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.helpers.SECOND
import com.rule.validator.formvalidator.FormValidator
import javax.inject.Inject

class PasswordResetViewModel @Inject constructor(private val loginManager: LoginManager) :
    BaseViewModel() {

    var emailVisibilityCallback: ((Boolean) -> Unit)? = null
    var codeVisibilityCallback: ((Boolean) -> Unit)? = null
    var passwordVisibilityCallback: ((Boolean) -> Unit)? = null

    val code = ObservableField<String>()

    var actionCallback: (() -> Unit)? = null
    var resetPasswordFinishedCallback: (() -> Unit)? = null

    val emailFormValidator = FormValidator(null)
    val passwordFormValidator = FormValidator(null)

    override fun onCleared() {
        super.onCleared()
        emailFormValidator.clear()
        passwordFormValidator.clear()
    }

    val currentState = object : MutableLiveData<State>() {
        override fun setValue(value: State?) {
            super.setValue(value)
            actionCallback?.invoke()
            when (value) {
                State.SEND_CODE_TO_EMAIL -> {
                    emailVisibilityCallback?.invoke(true)
                    codeVisibilityCallback?.invoke(false)
                    passwordVisibilityCallback?.invoke(false)
                }
                State.RESET_PASSWORD -> {
                    emailVisibilityCallback?.invoke(false)
                    codeVisibilityCallback?.invoke(true)
                    passwordVisibilityCallback?.invoke(false)
                }
                State.SAVE_NEW_PASSWORD -> {
                    emailVisibilityCallback?.invoke(false)
                    codeVisibilityCallback?.invoke(false)
                    passwordVisibilityCallback?.invoke(true)
                }
                else -> {
                    Log.i("currentState reset password", "else case")
                }
            }
        }
    }

    init {
        currentState.value = State.SEND_CODE_TO_EMAIL
    }

    fun resendCode(callback: () -> Unit) {
        callback.invoke()
        //makeRx(loginManager.requestResetPasswordCode(email.getValueOrEmpty()), callback)
    }

    fun requestSecureCode(email: String) {
           // makeRx(loginManager.requestResetPasswordCode(email)) {
                currentState.value = State.RESET_PASSWORD
          //  }

    }

    fun confirmSecureCode(email: String, code: String) {
        if (code.length >= SECURE_CODE_SIZE) {
            makeRx(loginManager.sendResetPasswordCode(email, code)) {
                currentState.setValue(State.SAVE_NEW_PASSWORD)
            }
        } else {
            showMessage(R.string.invalid_value)
        }
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
        showMessage(it.localizedMessage)
    }

    enum class State(@StringRes val actionButtonTitle: Int) {

        SEND_CODE_TO_EMAIL(R.string.send_secure_code_to_email),
        RESET_PASSWORD(R.string.reset_password),
        SAVE_NEW_PASSWORD(R.string.save_new_password)

    }

    companion object {
        private const val SECURE_CODE_SIZE = 6

        const val SECURE_CODE_ATTEMPT_DURATION = 31 * SECOND
    }

}