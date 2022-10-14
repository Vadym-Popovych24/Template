package com.android.template.data.remote.interfaces

import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.api.model.ProfileModel
import com.android.template.data.models.api.request.ChangePasswordRequest
import io.reactivex.Completable
import io.reactivex.Single

interface ProfileWebservice {
    fun getProfileInfo(profileId: String): Single<ProfileModel>
    fun updateProfile(profileSettings: ProfileSettings): Completable
    fun changePassword(changePasswordRequest: ChangePasswordRequest): Completable
}