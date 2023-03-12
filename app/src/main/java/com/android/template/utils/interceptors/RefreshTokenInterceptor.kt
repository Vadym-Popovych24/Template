package com.android.template.utils.interceptors

import com.android.template.utils.AppConstants
import okhttp3.*

class RefreshTokenInterceptor(private val refreshErrorCallback: () -> Unit) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val isAuthUrl = AppConstants.ENDPOINT_SERVER_LOGIN == request.url.toString()
        val isRefreshGrantType = request.body?.let {
            if (it is FormBody) {
                for (index in 0 until it.size - 1) {
                    if (it.encodedValue(index) == AppConstants.GRANT_REFRESH_TOKEN) {
                        return@let true
                    }
                }
            }
            false
        } ?: false
        val isRefreshTokenRequest = isAuthUrl && isRefreshGrantType
        val response = chain.proceed(request)
        if (isRefreshTokenRequest && 200 != response.code) {
            refreshErrorCallback()
        }
        return response
    }

}