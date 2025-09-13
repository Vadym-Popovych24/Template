package com.android.template.utils.interceptors

import com.android.template.data.prefs.PreferencesHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthDataInterceptor(private val preferences: PreferencesHelper): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        preferences.getToken()?.let { accessToken ->
            requestBuilder.header("Authorization", accessToken)
        }

        return chain.proceed(requestBuilder.build())
    }
}