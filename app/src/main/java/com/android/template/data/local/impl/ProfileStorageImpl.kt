package com.android.template.data.local.impl

import androidx.lifecycle.LiveData
import com.android.template.data.local.TemplateDatabase
import com.android.template.data.local.interfaces.ProfileStorage
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.db.Profile
import com.android.template.data.models.db.ProfileAndAvatar
import javax.inject.Inject

class ProfileStorageImpl @Inject constructor(private val database: TemplateDatabase) :
    ProfileStorage {
    override fun saveProfile(model: ProfileSettings) {
        val profile = Profile().apply {
            id = 1
            profileId = 1
            userName = model.userName
            email = model.email
            isEmailVerified = false
            firstName = model.firstName
            lastName = model.lastName
            phoneNumber = model.phoneNumber
            isEmployee = false
            isEmployee = false
            culture = model.culture
        }

        database.profileDao().insert(profile)
    }

    override fun getProfileById(profileId: Int): LiveData<ProfileAndAvatar> {
        return database.profileDao().getProfileById(profileId)
    }

}