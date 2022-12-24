package com.android.template.data.local.interfaces

import androidx.lifecycle.LiveData
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.db.ProfileAndAvatar

interface ProfileStorage {
    fun saveProfile(model: ProfileSettings)
    fun getProfileById(profileId: Int): LiveData<ProfileAndAvatar>
}