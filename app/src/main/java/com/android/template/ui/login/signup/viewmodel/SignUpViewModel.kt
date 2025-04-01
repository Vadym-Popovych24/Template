package com.android.template.ui.login.signup.viewmodel

import com.android.template.R
import com.android.template.data.models.api.model.SignUpProfileData
import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.rule.validator.formvalidator.FormValidator
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignUpViewModel @Inject constructor(private val loginManager: LoginManager) : BaseViewModel() {

    var requestToken: String? = null

    var signUpFinishedCallback: (() -> Unit)? = null

    val formValidator = FormValidator(null)

    override fun onCleared() {
        super.onCleared()
        formValidator.clear()
    }

    fun requestToken(email: String, completableCallback: ((String) -> Unit)) {
        makeRx(loginManager.requestToken(email)) {
            if (it.success) {
                requestToken = it.requestToken
                completableCallback.invoke(it.requestToken)
            } else {
                showMessage(R.string.failed_request_token)
            }
        }
    }

    fun approveToken(
        requestToken: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        makeRx(
            loginManager.authenticateAccount(
                requestToken = requestToken,
                signUpProfileData = SignUpProfileData(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    password = password
                )
            ), signUpFinishedCallback
        )
    }

    fun signUp(firstName: String, lastName: String, email: String, password: String, completableCallback: (() -> Unit)) {
        // Implement sign up
        makeRx(loginManager.signUp(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password
        ).delay(2, TimeUnit.SECONDS), completableCallback)
    }

    fun saveFCMToken(fcmToken: String, completeCallback: (() -> Unit)? = null) {
        makeRxInvisible(loginManager.saveFCMToken(fcmToken), completeCallback)
    }

}