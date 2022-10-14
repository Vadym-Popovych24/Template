package com.android.template.ui.login.reset.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.android.template.R
import com.android.template.data.repository.interfaces.LoginRepository
import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.getStringFromResource
import com.android.template.utils.getValueOrEmpty
import com.android.template.utils.helpers.SECOND
import com.android.template.utils.isEmail
import io.reactivex.Completable
import javax.inject.Inject

class PasswordResetViewModel @Inject constructor(private val loginManager: LoginManager) :
    BaseViewModel() {

    val email = ObservableField<String>()
    val emailError = ObservableField<String>()
    val emailVisible = ObservableBoolean()

    val code = ObservableField<String>()
    val codeVisible = ObservableBoolean()

    val passwordFieldsVisible = ObservableBoolean()

    val password = ObservableField<String>()
    val passwordError = ObservableField<String>()

    val confirmPassword = ObservableField<String>()
    val confirmPasswordError = ObservableField<String>()

    var actionCallback: (() -> Unit)? = null

    var resetPasswordFinishedCallback: (() -> Unit)? = null

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
            }
        }
    }

    init {
        currentState.value = State.SEND_CODE_TO_EMAIL
    }

    fun resendCode(callback: () -> Unit) = makeRx(loginManager.requestResetPasswordCode(email.getValueOrEmpty()), callback)

    fun action() {
        when (currentState.value) {
            State.SEND_CODE_TO_EMAIL -> requestSecureCode()
            State.RESET_PASSWORD -> confirmSecureCode()
            State.SAVE_NEW_PASSWORD -> saveNewPassword()
        }
    }

    private fun requestSecureCode() {
        emailError.set("")
        val email = email.getValueOrEmpty()
        if (email.isEmail()) {
            makeRx(loginManager.requestResetPasswordCode(email)) {
                currentState.value = State.RESET_PASSWORD
            }
        } else {
            emailError.set(R.string.invalid_value.getStringFromResource)
        }
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
        if (isPasswordsValid()) {
            makeRx(
                loginManager.resetPassword(
                    email.getValueOrEmpty(),
                    code.getValueOrEmpty(),
                    password.getValueOrEmpty()
                )
            ) {
                resetPasswordFinishedCallback?.invoke()
            }
        }
    }

    private fun isPasswordsValid(): Boolean = when {
        checkIfEmpty(password, passwordError) -> {
            false
        }
        checkIfEmpty(confirmPassword, confirmPasswordError) -> {
            false
        }
        password.getValueOrEmpty() != confirmPassword.getValueOrEmpty() -> {
            confirmPasswordError.set(R.string.passwords_not_match.getStringFromResource)
            false
        }
        else -> true
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