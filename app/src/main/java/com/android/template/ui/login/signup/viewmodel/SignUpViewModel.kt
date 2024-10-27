package com.android.template.ui.login.signup.viewmodel

import androidx.databinding.ObservableField
import com.android.template.data.models.exception.SignUpException
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.getStringFromResource
import com.rule.validator.formvalidator.FormValidator
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignUpViewModel @Inject constructor() :
    BaseViewModel() {

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

    fun signUp(completableCallback: ((String) -> Unit)) {
        // Implement sign up
        makeRx(Single.just("Registered successfully").map {
            it
        }.delay(2, TimeUnit.SECONDS), completableCallback)
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