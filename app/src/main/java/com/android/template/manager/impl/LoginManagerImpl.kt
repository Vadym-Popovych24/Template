package com.android.template.manager.impl

import com.android.template.data.models.api.model.SignUpProfileData
import com.android.template.data.models.api.response.RequestKeyResponse
import com.android.template.data.repository.interfaces.LoginRepository
import com.android.template.manager.interfaces.LoginManager
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class LoginManagerImpl @Inject constructor(private val loginRepository: LoginRepository) :
        BaseManagerImpl(loginRepository), LoginManager {

    override fun requestToken(email: String): Single<RequestKeyResponse> =
        loginRepository.requestToken(email)

    override fun authenticateAccount(requestToken: String, signUpProfileData: SignUpProfileData): Completable =
        loginRepository.authenticateAccount(requestToken, signUpProfileData)

    override fun authByDB(email: String, password: String): Completable =
        loginRepository.authByDB(email, password)

    override fun auth(email: String, password: String): Completable =
            loginRepository.auth(email, password)

    override fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Completable = loginRepository.signUp(firstName, lastName, email, password)

    override fun requestResetPasswordCode(email: String): Completable =
        loginRepository.requestResetPasswordCode(email)

    override fun sendResetPasswordCode(email: String, code: String): Completable =
        loginRepository.sendResetPasswordCode(email, code)

    override fun resetPassword(email: String, code: String, password: String): Completable =
        loginRepository.resetPassword(email, code, password)

    override fun saveFCMToken(fcmToken: String): Completable = loginRepository.saveFCMToken(fcmToken)
}