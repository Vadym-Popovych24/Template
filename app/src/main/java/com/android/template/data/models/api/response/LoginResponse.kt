package com.android.template.data.models.api.response

data class LoginResponse(val accessToken: String, val expiresIn: Long, val refreshToken: String)