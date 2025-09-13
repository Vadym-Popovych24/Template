package com.android.template.data.models.api.model

data class ProfileModel(
    val id: Int,
    val profileId: Int,
    val userName: String,
    val email: String,
    val isEmailVerified: Boolean,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val birthday: String?,
    val profilePictures: List<ProfilePictureModel>?,
    val isEmployee: Boolean,
    val position: String?,
    val culture: Int,
    val gender: Int
)

data class ProfilePictureModel(
    val profileId: Int,
    val pictureId: Int,
    val picture: PictureValue?
)

data class PictureValue(val altAttribute: String?)