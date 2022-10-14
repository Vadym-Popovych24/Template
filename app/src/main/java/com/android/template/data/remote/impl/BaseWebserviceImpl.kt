package com.android.template.data.remote.impl

import com.androidnetworking.error.ANError
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.UNAUTHORIZED
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

open class BaseWebserviceImpl @Inject constructor(private val authenticator: Authenticator) {

    protected fun <T> authorizedSingle(callback: (token: String) -> Single<T>) = Single.just(callback).flatMap {
        authenticator.getToken()?.let {
            callback(it)
        }
    }.retry(1) { throwable ->
        throwable is ANError && throwable.errorCode == UNAUTHORIZED
    }

    protected fun authorizedCompletable(callback: (token: String) -> Completable) = Single.just(callback).flatMapCompletable {
        authenticator.getToken()?.let {
            callback(it)
        }
    }.retry(1) { throwable ->
        throwable is ANError && throwable.errorCode == UNAUTHORIZED
    }

}