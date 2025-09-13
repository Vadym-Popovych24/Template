package com.android.template.data.models

import com.google.gson.annotations.SerializedName

class ProfileSettings(
    var firstName: String, var lastName: String, var birthday: String, var email: String,
    var phoneNumber: String?, var gender: Int?, var culture: Int?,
    @SerializedName("ProfileId") val profileId: Long
)