package com.android.template.data.models.api.request

import com.google.gson.annotations.SerializedName

data class SessionRequest(
    @SerializedName("request_token")
    private val requestToken: String
)