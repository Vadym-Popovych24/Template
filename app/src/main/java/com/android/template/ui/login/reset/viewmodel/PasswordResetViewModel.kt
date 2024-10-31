package com.android.template.ui.login.reset.viewmodel

import android.util.Log
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.android.template.R
import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.getValueOrEmpty
import com.android.template.utils.helpers.SECOND
import com.rule.validator.formvalidator.FormValidator
import javax.inject.Inject

class PasswordResetViewModel @Inject constructor(private val loginManager: LoginManager) :
    BaseViewModel() {


    val email = ObservableField<String>()
    val emailVisible = ObservableBoolean()

    val code = ObservableField<String>()
    val codeVisible = ObservableBoolean()

    val passwordFieldsVisible = ObservableBoolean()
    val password = ObservableField<String>()

    val confirmPassword = ObservableField<String>()

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
                    codeVisible.set(false)
                    passwordFieldsVisible.set(false)
                    emailVisible.set(true)
                }
                State.RESET_PASSWORD -> {
                    codeVisible.set(true)
                    passwordFieldsVisible.set(false)
                    emailVisible.set(false)
                }
                State.SAVE_NEW_PASSWORD -> {
                    codeVisible.set(false)
                    passwordFieldsVisible.set(true)
                    emailVisible.set(false)
                }
                else -> {
                    Log.i("currentState", "else case")
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

    fun action() {
        when (currentState.value) {
            State.SEND_CODE_TO_EMAIL -> requestSecureCode()
            State.RESET_PASSWORD -> confirmSecureCode()
            State.SAVE_NEW_PASSWORD -> saveNewPassword()
            else -> {
                Log.i("action", "else case")
            }
        }
    }

    private fun requestSecureCode() {

           // makeRx(loginManager.requestResetPasswordCode(email)) {
                currentState.value = State.RESET_PASSWORD
          //  }

    }

    private fun confirmSecureCode() {
        val code = code.getValueOrEmpty()
        if (code.length >= SECURE_CODE_SIZE) {
            makeRx(loginManager.sendResetPasswordCode(email.getValueOrEmpty(), code)) {
                currentState.setValue(State.SAVE_NEW_PASSWORD)
            }
        } else {
            showMessage(R.string.invalid_value)
        }
    }

    private fun saveNewPassword() {
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