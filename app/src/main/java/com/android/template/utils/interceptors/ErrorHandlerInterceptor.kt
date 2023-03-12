package com.android.template.utils.interceptors

import com.android.template.R
import com.android.template.utils.getStringFromResource
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandlerInterceptor(
        private val errorCodeCallback: ((Int) -> Unit)?
) : Interceptor {

    constructor():this(null)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return try {
            val response = chain.proceed(chain.request())
            validateResponse(response, request) ?: response
        } catch (e: UnknownHostException) {
            return getResponseWithError(NO_INTERNET, request)
        } catch (e: SocketTimeoutException) {
            return getResponseWithError(SOCKET_TIMEOUT, request)
        } catch (e: Exception) {
            e.printStackTrace()
            return getResponseWithError(UNKNOWN, request)
        }
    }

    private fun validateResponse(response: Response, request: Request): Response? {
        return when (response.code) {
            UNAUTHORIZED -> {
                errorCodeCallback?.let { it(UNAUTHORIZED) }
                return getResponseWithError(UNAUTHORIZED, request)
            }
            else -> null
        }
    }

    private fun getErrorMessage(errorCode: Int): String =
            when (errorCode) {
                UNAUTHORIZED -> R.string.unauthorized_error
                NO_INTERNET -> R.string.no_internet_error
                SOCKET_TIMEOUT -> R.string.timeout_error
                else -> R.string.default_error
            }.getStringFromResource

    private fun getResponseWithError(code: Int, request: Request): Response {
        val errorMessage = getErrorMessage(code)
        return Response.Builder()
                .code(code)
                .message(errorMessage)
                .request(request)
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), errorMessage))
                .addHeader("content-type", "application/json")
                .build()
    }

    companion object {

        /**
         * Custom Errors
         */

        const val INVALID_USERNAME_OR_PASSWORD = 400
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val NO_INTERNET = 410
        const val SOCKET_TIMEOUT = 411
        const val UNKNOWN = 499
        const val INTERNAL_SERVER_ERROR = 500
    }
}