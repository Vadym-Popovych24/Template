package com.android.template.ui.login.signup.viewmodel

import androidx.databinding.ObservableField
import com.android.template.data.models.exception.SignUpException
import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.getStringFromResource
import com.rule.validator.formvalidator.FormValidator
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val loginManager: LoginManager) : BaseViewModel() {

    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    val email = ObservableField<String>()
    val password = ObservableField<String>()
    val confirmPassword = ObservableField<String>()

    var signUpFinishedCallback: (() -> Unit)? = null

    val formValidator = FormValidator(null)

    override fun onCleared() {
        super.onCleared()
        formValidator.clear()
    }

    fun signUp(firstName: String, lastName: String, email: String, password: String, completableCallback: (() -> Unit)) {
        // Implement sign up
        makeRx(loginManager.signUp(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password,
        ).delay(2, TimeUnit.SECONDS), completableCallback)
    }

    override fun handleError(it: Throwable) {
        if (it is SignUpException) {
            it.errors.forEach {
                val errorMessage = it.errorMessage.getStringFromResource
                showMessage(errorMessage)
            }
        } else super.handleError(it)
    }

}