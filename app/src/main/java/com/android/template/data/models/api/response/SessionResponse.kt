package com.android.template.data.models.api.response

import com.google.gson.annotations.SerializedName

data class SessionResponse(
    val success: Boolean,
    @SerializedName("session_id")
    val sessionId: String
)