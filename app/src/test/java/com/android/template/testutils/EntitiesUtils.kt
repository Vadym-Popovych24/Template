package com.android.template.testutils

import com.android.template.data.models.api.model.SignUpProfileData
import com.android.template.data.models.api.response.AccountResponse
import com.android.template.data.models.api.response.Avatar
import com.android.template.data.models.api.response.Gravatar
import com.android.template.data.models.api.response.RequestKeyResponse
import com.android.template.data.models.api.response.SessionResponse
import com.android.template.data.models.api.response.Tmdb
import com.android.template.data.models.db.ProfileEntity

fun createProfileEntity(email: String, password: String = "123456Q") =
    ProfileEntity(
        id = 1,
        profileId = 1,
        email = email,
        phoneNumber = null,
        gender = null,
        culture = null,
        birthday = null,
        firstName = "name",
        lastName = "lastName",
        userName = "userName",
        password = password,
        avatarPath = "avatar_path",
        coverPath = null,
        requestToken = "request_token",
        sessionId = "sessionId123"
    )

fun createRequestKeyResponse() =
    RequestKeyResponse(
        success = true,
        expiresAt = "2025-12-31T23:59:59Z",
        requestToken = "new_request_token"
    )

fun createSignUpProfileData() = SignUpProfileData(
    firstName = "Name",
    lastName = "LastName",
    email = "test@example.com",
    password = "password"
)

fun createAccountResponse(): AccountResponse {
    val gravatar = Gravatar(hash = "hash")
    val tmdb = Tmdb(avatarPath = "avatar_path")
    val avatar = Avatar(gravatar = gravatar, tmdb = tmdb)

    return AccountResponse(
        avatar = avatar,
        id = 1,
        name = "name",
        username = "username",
        iso6391 = "iso6391",
        iso31661 = "iso31661",
        includeAdult = false
    )
}

fun createSessionResponse(sessionId: String) = SessionResponse(
    success = true,
    sessionId = sessionId
)