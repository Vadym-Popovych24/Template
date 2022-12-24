package com.android.template.ui.login.signup.viewmodel

import androidx.databinding.ObservableField
import com.android.template.R
import com.android.template.data.models.enums.SignUpError
import com.android.template.data.models.exception.SignUpException
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.getStringFromResource
import com.android.template.utils.getValueOrEmpty
import com.android.template.utils.isEmail
import javax.inject.Inject

class SignUpViewModel @Inject constructor() :
    BaseViewModel() {

    val firstName = ObservableField<String>()
    val firstNameError = ObservableField<String>()

    val lastName = ObservableField<String>()
    val lastNameError = ObservableField<String>()

    val email = ObservableField<String>()
    val emailError = ObservableField<String>()

    val password = ObservableField<String>()
    val passwordError = ObservableField<String>()

    val confirmPassword = ObservableField<String>()
    val confirmPasswordError = ObservableField<String>()

    var signUpFinishedCallback: (() -> Unit)? = null

    fun signUp() {
        if (isDataValid()) {
            // TODO implement sign up request
        }
    }

    private fun isDataValid(): Boolean {
        emailError.set("")

        val email = email.getValueOrEmpty()
        return if (checkIfEmpty(firstName, firstNameError)) {
            false
        } else if (checkIfEmpty(lastName, lastNameError)) {
            false
        } else if (!email.isEmail()) {
            emailError.set(R.string.invalid_value.getStringFromResource)
            false
        } else if (checkIfEmpty(password, passwordError, PASSWORD_LENGTH)) {
            false
        } else if (checkIfEmpty(confirmPassword, confirmPasswordError, PASSWORD_LENGTH)) {
            false
        } else if (password.getValueOrEmpty() != confirmPassword.getValueOrEmpty()) {
            confirmPasswordError.set(R.string.passwords_not_match.getStringFromResource)
            false
        } else true
    }

    override fun handleError(it: Throwable) {
        if (it is SignUpException) {
            it.errors.forEach {
                val errorMessage = it.errorMessage.getStringFromResource
                when (it) {
                    SignUpError.EMAIL_ALREADY_TAKEN -> emailError.set(errorMessage)
                    SignUpError.REQUIRES_DIGIT -> passwordError.set(errorMessage)
                    SignUpError.REQUIRES_UPPER -> passwordError.set(errorMessage)
                    SignUpError.REQUIRES_LOVER -> passwordError.set(errorMessage)
                }
            }
        } else super.handleError(it)
    }


    companion object {
        private const val PASSWORD_LENGTH = 6
    }


}