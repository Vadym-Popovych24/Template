package com.android.template.data.remote.impl

import com.android.template.R
import com.android.template.data.models.api.request.SessionRequest
import com.android.template.data.models.api.response.LoginResponse
import com.android.template.data.models.api.response.RequestKeyResponse
import com.android.template.data.models.api.response.SessionResponse
import com.android.template.data.models.exception.ApproveException
import com.android.template.data.remote.api.ApiEndpoints
import com.android.template.data.remote.api.LoginApi
import com.android.template.data.remote.api.WebApi
import com.android.template.data.remote.interfaces.LoginWebservice
import com.android.template.di.qualifiers.BaseRetrofit
import com.android.template.di.qualifiers.WebRetrofit
import com.android.template.utils.getStringFromResource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import javax.inject.Inject

class LoginWebserviceImpl @Inject constructor(
    @BaseRetrofit baseRetrofit: Retrofit,
    @WebRetrofit webRetrofit: Retrofit
) : LoginWebservice {

    private val loginApi = baseRetrofit.create(LoginApi::class.java)
    private val webApi = webRetrofit.create(WebApi::class.java)

    override fun requestToken(): Single<RequestKeyResponse> =
        loginApi.requestToken(ApiEndpoints.API_KEY)

    override fun approveRequestToken(requestToken: String): Completable =
        webApi.approveRequestToken(requestToken)
        .onErrorResumeNext { throwable ->
            Completable.create {
                if (throwable is retrofit2.HttpException && throwable.code() == 401) {
                    it.onError(ApproveException())
                } else {
                    it.onError(throwable)
                }
            }
        }

    override fun createSession(requestToken: String): Single<SessionResponse> =
        loginApi.createSession(ApiEndpoints.API_KEY, SessionRequest(requestToken))

    override fun login(email: String, password: String): Single<LoginResponse> =
        TODO("Change it by yours api request")

    override fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Single<LoginResponse> =
        TODO("Change it by yours api request")
        // Below example how to handle custom error response from server
        /*.onErrorResumeNext { throwable ->
                Single.create {
                    if (throwable is HttpException) {
                        try {
                            val signUpErrorResponse = gson.fromJson(
                                throwable.message, SignUpErrorResponse::class.java
                            )
                            if (signUpErrorResponse != null) {
                                it.onError(SignUpException(signUpErrorResponse.getFormattedErrors()))
                            } else {
                                it.onError(throwable)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            it.onError(throwable)
                            throw throwable
                        }
                    } else {
                        it.onError(throwable)
                    }
                }
            }*/

    override fun requestResetPasswordCode(email: String): Completable =
        Completable.complete() // TODO Change it by yours api request

    override fun sendResetPasswordCode(email: String, code: String): Completable =
        if (code == "123456") {
            Completable.complete()
        } else {
            Completable.error(
                IllegalArgumentException(
                    R.string.wrong_secure_code_error.getStringFromResource
                )
            )
        } // TODO Change it by yours api request

    override fun resetPassword(
        email: String,
        code: String,
        password: String
    ): Single<LoginResponse> =
        Single.just(LoginResponse("", 0,"")) // TODO Change it by yours api request
   .onErrorResumeNext {
                Single.error(
                    IllegalArgumentException(
                        R.string.wrong_password_error.getStringFromResource
                    )
                )
            }
}