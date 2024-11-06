package com.android.template.data.remote.impl

import com.android.template.R
import com.android.template.data.models.api.request.DeviceLoginRequest
import com.android.template.data.models.api.response.LoginResponse
import com.android.template.data.models.api.response.SignUpErrorResponse
import com.android.template.data.models.exception.SignUpException
import com.android.template.data.remote.interfaces.LoginWebservice
import com.android.template.utils.AppConstants
import com.android.template.utils.getStringFromResource
import com.androidnetworking.error.ANError
import com.google.gson.Gson
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class LoginWebserviceImpl @Inject constructor(private val gson: Gson) : LoginWebservice {

    override fun loginApiCall(username: String, password: String): Single<LoginResponse> =
        Rx2AndroidNetworking.post(AppConstants.ENDPOINT_SERVER_LOGIN)
            .addHeaders(contentType, contentTypeValue)
            .addBodyParameter("UserName", username)
            .addBodyParameter("password", password)
            .addBodyParameter("grant_type", AppConstants.GRANT_PASSWORD)
            .addBodyParameter("client_id", AppConstants.CLIENT_ID)
            .addBodyParameter("client_secret", AppConstants.CLIENT_SECRET)
            .build()
            .getObjectSingle(LoginResponse::class.java)

    override fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Single<LoginResponse> =
        Rx2AndroidNetworking.post(AppConstants.ENDPOINT_SIGN_UP)
            .addHeaders("Content-Type", "application/x-www-form-urlencoded")
            .addBodyParameter("firstName", firstName)
            .addBodyParameter("lastName", lastName)
            .addBodyParameter("email", email)
            .addBodyParameter("password", password)
            .addBodyParameter("grant_type", AppConstants.GRANT_PASSWORD)
            .addBodyParameter("client_id", AppConstants.CLIENT_ID)
            .addBodyParameter("client_secret", AppConstants.CLIENT_SECRET)
            .build()
            .getObjectSingle(LoginResponse::class.java)
            .onErrorResumeNext { throwable ->
                Single.create {
                    if (throwable is ANError) {
                        try {
                            val signUpErrorResponse = gson.fromJson(
                                throwable.errorBody, SignUpErrorResponse::class.java
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
            }

    override fun loginDevice(request: DeviceLoginRequest, token: String): Completable =
        Rx2AndroidNetworking.post(AppConstants.ENDPOINT_DEVICE_LOGIN)
            .addApplicationJsonBody(request)
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .stringCompletable

    override fun requestResetPasswordCode(email: String): Completable =
        Rx2AndroidNetworking.post(AppConstants.ENDPOINT_RESET_PASSWORD_GET_CODE)
            .addHeaders(contentType, contentTypeValue)
            .addBodyParameter("email", email)
            .build()
            .stringCompletable
            .onErrorResumeNext {
                Completable.error(
                    IllegalArgumentException(
                        R.string.no_user_with_email_error.getStringFromResource.format(
                            email
                        )
                    )
                )
            }

    override fun sendResetPasswordCode(email: String, code: String): Completable =
        if (code == "123456") {
            Completable.complete()
        } else {
            Completable.error(
                IllegalArgumentException(
                    R.string.wrong_secure_code_error.getStringFromResource
                )
            )
        }
        // Uncomment and edit it for your real API call
        /*Rx2AndroidNetworking.post(AppConstants.ENDPOINT_RESET_PASSWORD_SUBMIT_CODE)
            .addHeaders(contentType, contentTypeValue)
            .addBodyParameter("email", email)
            .addBodyParameter("code", code)
            .build()
            .stringCompletable
            .onErrorResumeNext {
                Completable.error(
                    IllegalArgumentException(
                        R.string.wrong_secure_code_error.getStringFromResource
                    )
                )
            }*/

    override fun resetPassword(
        email: String,
        code: String,
        password: String
    ): Single<LoginResponse> =
        Rx2AndroidNetworking.post(AppConstants.ENDPOINT_RESET_PASSWORD)
            .addHeaders(contentType, contentTypeValue)
            .addBodyParameter("email", email)
            .addBodyParameter("code", code)
            .addBodyParameter("password", password)
            .addBodyParameter("grant_type", AppConstants.GRANT_PASSWORD)
            .addBodyParameter("client_id", AppConstants.CLIENT_ID)
            .addBodyParameter("client_secret", AppConstants.CLIENT_SECRET)
            .build()
            .getObjectSingle(LoginResponse::class.java)
            .onErrorResumeNext {
                Single.error(
                    IllegalArgumentException(
                        R.string.wrong_password_error.getStringFromResource
                    )
                )
            }

    companion object {
       private const val contentType = "Content-Type"
       private const val contentTypeValue = "application/x-www-form-urlencoded"
    }
}