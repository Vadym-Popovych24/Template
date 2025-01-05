package com.android.template.data.local.impl

import androidx.lifecycle.LiveData
import androidx.room.rxjava3.EmptyResultSetException
import com.android.template.data.local.TemplateDatabase
import com.android.template.data.local.interfaces.ProfileStorage
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.db.ProfileEntity
import com.android.template.data.models.db.ProfileAndAvatar
import com.android.template.data.models.exception.UserNotFoundException
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ProfileStorageImpl @Inject constructor(private val database: TemplateDatabase) :
    ProfileStorage {

    override fun insertProfile(profileEntity: ProfileEntity) {
        database.profileDao().insert(profileEntity)
    }

    override fun updateProfile(profileSettings: ProfileSettings, originalEmail: String) {
        database.profileDao().updateProfile(
            firstName = profileSettings.firstName,
            lastName = profileSettings.lastName,
            birthday = profileSettings.birthday,
            email = profileSettings.email,
            phoneNumber = profileSettings.phoneNumber,
            userName = profileSettings.userName,
            gender = profileSettings.gender,
            culture = profileSettings.culture,
            originalEmail = originalEmail
        )
    }

    override fun updatePassword(oldPassword: String, newPassword: String): Single<Int> =
        database.profileDao().updatePassword(oldPassword, newPassword)

    override fun getProfileById(profileId: Int): LiveData<ProfileAndAvatar> =
        database.profileDao().getProfileById(profileId)

    override fun getProfileByEmail(email: String): Single<ProfileEntity> =
        database.profileDao().getProfileByEmail(email)
            .onErrorResumeNext {
                if (it is EmptyResultSetException) {
                    Single.error(UserNotFoundException())
                } else {
                    Single.error(it)
                }
            }

    override fun getProfileByEmailIgnoreEmpty(email: String): Single<ProfileEntity> =
        database.profileDao().getProfileByEmail(email)
            .onErrorReturn {
                ProfileEntity.getEmptyProfileEntity()
            }

}