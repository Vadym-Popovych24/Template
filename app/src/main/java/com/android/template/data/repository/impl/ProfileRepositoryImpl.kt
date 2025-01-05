package com.android.template.data.repository.impl

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.security.crypto.EncryptedFile
import com.android.template.data.local.interfaces.ProfileStorage
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.api.model.ProfileMenuModel
import com.android.template.data.models.db.ProfileAndAvatar
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.data.remote.interfaces.RemoteFileWebservice
import com.android.template.data.repository.interfaces.ProfileRepository
import com.android.template.utils.encryptedFile
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.File
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val storage: ProfileStorage,
    private val preferences: PreferencesHelper,
    private val remoteFileWebservice: RemoteFileWebservice,
    private val cacheDir: File
) : BaseRepositoryImpl(), ProfileRepository {

    override fun getProfile(): LiveData<ProfileAndAvatar> {
        val id = preferences.getProfileId()
        return storage.getProfileById(id)
    }

    override fun getProfileSettings(): Single<ProfileSettings> =
        storage.getProfileByEmail(preferences.getEmail().toString()).map {
            it.culture?.let { culture -> preferences.setLanguageCode(culture) }
            ProfileSettings(
                firstName = it.firstName,
                lastName = it.lastName,
                birthday = it.birthday ?: "",
                email = it.email,
                phoneNumber = it.phoneNumber,
                userName = it.userName,
                gender = it.gender,
                culture = it.culture,
                profileId = it.profileId
            )
        }

    override fun updateProfile(profileSettings: ProfileSettings): Completable =
        Completable.fromAction {
            storage.updateProfile(profileSettings, preferences.getEmail().toString())
            profileSettings.culture?.let { preferences.setLanguageCode(it) }
            preferences.setUserName(profileSettings.userName)
            preferences.setEmail(profileSettings.email)
            profileSettings.culture?.let { preferences.setLanguageCode(it) }
        }

    override fun changePassword(oldPassword: String, newPassword: String): Single<Int> =
        storage.updatePassword(oldPassword, newPassword)


    override fun uploadAvatar(bitmap: Bitmap): Single<String> {
        val imageFile = File(cacheDir, "cached_avatar.jpg")

        val encryptedFile = imageFile.encryptedFile()
        return writeAvatarToCache(encryptedFile, bitmap, imageFile.path).flatMap {
            remoteFileWebservice.uploadAvatar(it)
        }.map {
            preferences.setUserAvatar(it)
            imageFile.delete()
            it
        }.doOnSuccess {
            imageFile.delete()
        }.doOnError {
            imageFile.delete()
        }
    }

    override fun getProfileHeader(): Single<ProfileMenuModel> = Single.just(preferences).map {
        ProfileMenuModel(
            it.getEmail().toString(),
            it.getUserAvatar().toString(),
            it.getUserName().toString()
        )
    }

    private fun writeAvatarToCache(encryptedFile: EncryptedFile, bitmap: Bitmap, path: String): Single<String> =
        Single.just(bitmap).map {
            val os = encryptedFile.openFileOutput()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            path
        }

    override fun logout(): Completable = Completable.fromAction {
        preferences.clearAllPreferences()
    }
}