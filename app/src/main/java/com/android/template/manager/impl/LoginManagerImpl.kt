package com.android.template.manager.impl

import com.android.template.data.repository.interfaces.LoginRepository
import com.android.template.manager.interfaces.LoginManager
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class LoginManagerImpl @Inject constructor(private val loginRepository: LoginRepository) :
        BaseManagerImpl(loginRepository), LoginManager {

    override fun auth(username: String, password: String): Completable =
            loginRepository.auth(username, password)

    override fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Completable = loginRepository.signUp(firstName, lastName, email, password)

    override fun getCachedEmail(): Single<String> =
        loginRepository.getCachedEmail()

    override fun requestResetPasswordCode(email: String): Completable =
        loginRepository.requestResetPasswordCode(email)

    override fun sendResetPasswordCode(email: String, code: String): Completable =
        loginRepository.sendResetPasswordCode(email, code)

    override fun resetPassword(email: String, code: String, password: String): Completable =
        loginRepository.resetPassword(email, code, password)
}