package com.android.template.data.local.interfaces

import androidx.lifecycle.LiveData
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.db.ProfileAndAvatar
import com.android.template.data.models.db.ProfileEntity
import io.reactivex.rxjava3.core.Single

interface ProfileStorage {
    fun insertProfile(profileEntity: ProfileEntity)
    fun updateProfile(profileSettings: ProfileSettings, originalEmail: String)
    fun updatePassword(oldPassword: String, newPassword: String): Single<Int>
    fun getProfileLiveDataById(id: Long): LiveData<ProfileAndAvatar>
    fun getProfileByEmail(email: String): Single<ProfileEntity>
    fun getProfileByEmailIgnoreEmpty(email: String): Single<ProfileEntity>
    fun getProfileIdByEmail(email: String): Long
}