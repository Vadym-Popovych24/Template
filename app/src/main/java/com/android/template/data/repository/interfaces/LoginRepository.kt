package com.android.template.data.repository.interfaces

import io.reactivex.Completable
import io.reactivex.Single

interface LoginRepository : BaseRepository {
    fun auth(username: String, password: String): Completable
    fun signUp(firstName: String, lastName: String, email: String, password: String): Completable
    fun getCachedEmail(): Single<String>
    fun requestResetPasswordCode(email: String): Completable
    fun sendResetPasswordCode(email: String, code: String): Completable
    fun resetPassword(email: String, code: String, password: String): Completable
}