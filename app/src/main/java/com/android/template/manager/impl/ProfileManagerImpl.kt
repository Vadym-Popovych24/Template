package com.android.template.manager.impl

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.api.model.CoverPicture
import com.android.template.data.models.api.model.PictureValue
import com.android.template.data.models.api.model.ProfileMenuModel
import com.android.template.data.models.api.request.ChangePasswordRequest
import com.android.template.data.models.db.ProfileAndAvatar
import com.android.template.data.repository.interfaces.ProfileRepository
import com.android.template.manager.interfaces.ProfileManager
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ProfileManagerImpl @Inject constructor(
    private val profileRepository: ProfileRepository
) : BaseManagerImpl(profileRepository),
    ProfileManager {

    override fun getProfileAPI(): Single<CoverPicture> =
        Single.just(CoverPicture(1, PictureValue("attr")))

    override fun getProfile(): LiveData<ProfileAndAvatar> =
        profileRepository.getProfile()

    override fun updateProfile(profileSettings: ProfileSettings): Completable =
        profileRepository.updateProfile(profileSettings)

    override fun changePassword(changePasswordRequest: ChangePasswordRequest): Completable =
        profileRepository.changePassword(changePasswordRequest)

    override fun getProfileHeader(): Single<ProfileMenuModel> =
        profileRepository.getProfileHeader()

    override fun getProfileSettings(): Single<ProfileSettings> =
        profileRepository.getProfileSettings()

    override fun uploadAvatar(bitmap: Bitmap): Single<String> =
        profileRepository.uploadAvatar(bitmap)

    override fun logout(): Completable = profileRepository.logout()
}