package com.android.template.data.models.api.response

data class LoginResponse(val accessToken: String, val expiresIn: Int, val refreshToken: String)