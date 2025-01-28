package com.android.template.data.repository.impl

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.security.crypto.EncryptedFile
import com.android.template.data.local.TemplateDatabase
import com.android.template.data.local.interfaces.ProfilesStorage
import com.android.template.data.models.ProfileSettings
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
    private val storage: ProfilesStorage,
    private val preferences: PreferencesHelper,
    private val remoteFileWebservice: RemoteFileWebservice,
    private val cacheDir: File,
    private val database: TemplateDatabase,
) : BaseRepositoryImpl(), ProfileRepository {

    override fun getProfile(): LiveData<ProfileAndAvatar> {
        val id = preferences.getDBProfileId()
        return storage.getProfileLiveDataById(id)
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
                gender = it.gender,
                culture = it.culture,
                profileId = it.profileId
            )
        }

    override fun updateProfile(profileSettings: ProfileSettings): Completable =
        Completable.fromAction {
            storage.updateProfile(profileSettings, preferences.getEmail().toString())
            profileSettings.culture?.let { preferences.setLanguageCode(it) }
            preferences.setEmail(profileSettings.email)
            profileSettings.culture?.let { preferences.setLanguageCode(it) }
        }

    override fun changePassword(oldPassword: String, newPassword: String): Single<Int> =
        storage.updatePassword(oldPassword, newPassword)

    override fun updateAvatar(avatarPath: String): Single<Int> {
        val id = preferences.getDBProfileId()
        return storage.updateAvatar(avatarPath, id)
    }

    override fun updateCover(coverPath: String): Single<Int> {
        val id = preferences.getDBProfileId()
        return storage.updateCover(coverPath, id)
    }

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
        // TODO uncomment for real project database.clearAllTables()
    }
}