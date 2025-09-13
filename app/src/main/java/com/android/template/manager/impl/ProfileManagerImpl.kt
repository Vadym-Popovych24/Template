package com.android.template.manager.impl

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.db.ProfileAndAvatar
import com.android.template.data.repository.interfaces.ProfileRepository
import com.android.template.manager.interfaces.ProfileManager
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ProfileManagerImpl @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseManagerImpl(profileRepository),
    ProfileManager {

    override fun getProfile(): LiveData<ProfileAndAvatar> =
        profileRepository.getProfile()

    override fun updateProfile(profileSettings: ProfileSettings): Completable =
        profileRepository.updateProfile(profileSettings)

    override fun changePassword(oldPassword: String, newPassword: String): Single<Int> =
        profileRepository.changePassword(oldPassword, newPassword)

    override fun updateAvatar(avatarPath: String): Single<Int> =
        profileRepository.updateAvatar(avatarPath)

    override fun updateCover(coverPath: String): Single<Int> =
        profileRepository.updateCover(coverPath)

    override fun getProfileSettings(): Single<ProfileSettings> =
        profileRepository.getProfileSettings()

    override fun uploadAvatar(bitmap: Bitmap): Single<String> =
        profileRepository.uploadAvatar(bitmap)

    override fun logout(): Completable = profileRepository.logout()
}