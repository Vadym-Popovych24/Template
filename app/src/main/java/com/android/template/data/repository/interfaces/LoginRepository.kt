package com.android.template.data.repository.interfaces

import com.android.template.data.models.api.request.ServerLoginRequest
import com.android.template.data.models.api.response.LoginResponse
import io.reactivex.Completable
import io.reactivex.Single

interface LoginRepository : BaseRepository {
    fun auth(username: String, password: String): Completable
    fun signUp(serverLoginRequest: ServerLoginRequest): Completable
    fun getCachedEmail(): Single<String>
    fun requestResetPasswordCode(email: String): Completable
    fun sendResetPasswordCode(email: String, code: String): Completable
    fun resetPassword(email: String, code: String, password: String): Completable
}