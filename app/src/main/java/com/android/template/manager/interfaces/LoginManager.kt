package com.android.template.manager.interfaces

import com.android.template.data.models.api.model.SignUpProfileData
import com.android.template.data.models.api.response.RequestKeyResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface LoginManager : BaseManager {

    fun requestToken(email: String): Single<RequestKeyResponse>

    fun authenticateAccount(requestToken: String, signUpProfileData: SignUpProfileData): Completable

    fun authByDB(email: String, password: String): Completable

    fun auth(email: String, password: String): Completable

    fun signUp(firstName: String, lastName: String, email: String, password: String): Completable

    fun requestResetPasswordCode(email: String): Completable

    fun sendResetPasswordCode(email: String, code: String): Completable

    fun resetPassword(email: String, code: String, password: String): Completable
}