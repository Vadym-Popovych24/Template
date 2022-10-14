package com.android.template.data.models.api.response

import com.google.gson.annotations.Expose

class LoginResponse {
    @Expose
    private val accessToken: String? = null

    @Expose
    private val expiresIn: Int? = 0

    @Expose
    private val tokenType: String? = null

    @Expose
    private val refreshToken: String? = null

    @Expose
    private val error: String? = null

    @Expose
    private val errorDescription: String? = null

    fun getToken() = accessToken

    fun getRefreshToken() = refreshToken

    fun getValidityPeriod() = expiresIn
}