package com.android.template.utils.interceptors

import android.util.Log
import com.android.template.data.models.api.response.LoginResponse
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.utils.AppConstants
import com.androidnetworking.error.ANError
import com.rx2androidnetworking.Rx2AndroidNetworking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator constructor(
    private val preferences: PreferencesHelper,
    private val onInvalidRefreshToken: () -> Unit
) :
    Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("TOKEN_AUTHENTICATOR", "authenticating")

        val refreshToken = preferences.getRefreshToken().toString()

        val newToken = refreshToken(refreshToken)

        if (newToken.isEmpty()) { // refresh token is invalid
            onInvalidRefreshToken()
            return null
        }

        Log.d("TOKEN_AUTHENTICATOR", "authenticated token $newToken, repeating call")

        return response.request.newBuilder()
            .header("Authorization", "Bearer $newToken")
            .build()
    }

    private fun refreshToken(refreshToken: String): String {
        return Rx2AndroidNetworking.post(AppConstants.ENDPOINT_SERVER_LOGIN)
            .addHeaders("Content-Type", "application/x-www-form-urlencoded")
            .addBodyParameter("refresh_token", refreshToken)
            .addBodyParameter("grant_type", AppConstants.GRANT_REFRESH_TOKEN)
            .addBodyParameter("client_id", AppConstants.CLIENT_ID)
            .addBodyParameter("client_secret", AppConstants.CLIENT_SECRET)
            .build()
            .getObjectSingle(LoginResponse::class.java)
            .map { loginResponse ->
                preferences.setToken(loginResponse.accessToken)
                preferences.setRefreshToken(loginResponse.refreshToken)

                loginResponse.accessToken
            }
            .onErrorReturn { error ->
                if (error is ANError && error.errorBody.contains("invalid_grant")) {
                    ""
                } else {
                    throw error
                }
            }
            .blockingGet()
    }
}