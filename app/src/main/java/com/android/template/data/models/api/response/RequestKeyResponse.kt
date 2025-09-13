package com.android.template.data.models.api.response

import com.google.gson.annotations.SerializedName

data class RequestKeyResponse(
    val success: Boolean,

    @SerializedName("expires_at")
    val expiresAt: String,

    @SerializedName("request_token")
    val requestToken: String
)