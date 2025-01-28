package com.android.template.utils.interceptors

import android.util.Log
import com.android.template.data.prefs.PreferencesHelper
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
        return ""
        /* TODO Here you can implement API request - fun refreshLogin(): Single<LoginResponse>
            .map { loginResponse ->
                preferences.setToken(loginResponse.accessToken)
                preferences.setRefreshToken(loginResponse.refreshToken)
                loginResponse.accessToken
            }
            .onErrorReturn { error ->
                if (error is HttpException && error.message.contains("invalid_grant")) {
                    ""
                } else {
                    throw error
                }
            }
            .blockingGet()
    }*/
    }
}