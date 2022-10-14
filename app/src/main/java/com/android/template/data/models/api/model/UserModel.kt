package com.android.template.data.models.api.model

import com.google.gson.annotations.Expose

data class UserModel(
    @Expose
    val email: String,

    @Expose
    val profileId: Int,

    @Expose
    val avatarUrl: String,

    @Expose
    val firstName: String,

    @Expose
    val lastName: String,

    @Expose
    val isEmailVerified: Boolean
)