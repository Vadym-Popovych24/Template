package com.android.template.data.models.api.request

class ChangePasswordRequest(
    val confirmPassword: String,
    val newPassword: String,
    val oldPassword: String
)