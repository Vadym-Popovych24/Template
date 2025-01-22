package com.android.template.data.remote.interfaces

import com.android.template.data.models.api.response.LoginResponse
import com.android.template.data.models.api.response.RequestKeyResponse
import com.android.template.data.models.api.response.SessionResponse
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface LoginWebservice {

    fun requestToken(): Single<RequestKeyResponse>

    fun approveRequestToken(requestToken: String): Completable

    fun createSession(requestToken: String): Single<SessionResponse>

    fun login(email: String, password: String): Single<LoginResponse>

    fun signUp(firstName: String, lastName: String, email: String, password: String): Single<LoginResponse>

    fun requestResetPasswordCode(email: String): Completable

    fun sendResetPasswordCode(email: String, code: String): Completable

    fun resetPassword(email: String, code: String, password: String): Single<LoginResponse>
}