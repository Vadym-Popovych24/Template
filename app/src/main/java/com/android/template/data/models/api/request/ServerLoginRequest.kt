package com.android.template.data.models.api.request

data class ServerLoginRequest(
    val firstName: String?= null,
    val lastName: String? = null,
    val phone: String?= null,
    val password: String?= null,
    val createdDate: String?= null
)