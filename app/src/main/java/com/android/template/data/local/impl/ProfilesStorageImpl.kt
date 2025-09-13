package com.android.template.data.local.impl

import androidx.lifecycle.LiveData
import androidx.room.rxjava3.EmptyResultSetException
import com.android.template.data.local.TemplateDatabase
import com.android.template.data.local.interfaces.ProfilesStorage
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.db.ProfileEntity
import com.android.template.data.models.db.ProfileAndAvatar
import com.android.template.data.models.exception.UserNotFoundException
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ProfilesStorageImpl @Inject constructor(private val database: TemplateDatabase) :
    ProfilesStorage {

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
            gender = profileSettings.gender,
            culture = profileSettings.culture,
            originalEmail = originalEmail
        )
    }

    override fun updatePassword(oldPassword: String, newPassword: String): Single<Int> =
        database.profileDao().updatePassword(oldPassword, newPassword)

    override fun updateAvatar(avatarPath: String, id: Long): Single<Int> =
        database.profileDao().updateAvatar(avatarPath, id)

    override fun updateCover(coverPath: String, id: Long): Single<Int> =
        database.profileDao().updateCover(coverPath, id)

    override fun getProfileLiveDataById(id: Long): LiveData<ProfileAndAvatar> =
        database.profileDao().getProfileLiveDataById(id)

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

    override fun getProfileIdByEmail(email: String): Long =
        database.profileDao().getProfileIdByEmail(email)

}