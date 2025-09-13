package com.android.template.data.models.api.response

import com.google.gson.annotations.SerializedName

data class AccountResponse(
    val avatar: Avatar,
    val id: Long,
    val name: String,
    val username: String,
    @SerializedName("iso_639_1") val iso6391: String,
    @SerializedName("iso_3166_1") val iso31661: String,
    @SerializedName("include_adult") val includeAdult: Boolean
)

data class Avatar(val gravatar: Gravatar, val tmdb: Tmdb)

data class Gravatar(val hash: String)

data class Tmdb( @SerializedName("avatar_path") val avatarPath: String?)