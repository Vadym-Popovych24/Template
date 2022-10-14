package com.android.template.manager.interfaces

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.api.model.CoverPicture
import com.android.template.data.models.api.model.ProfileMenuModel
import com.android.template.data.models.api.request.ChangePasswordRequest
import com.android.template.data.models.db.ProfileAndAvatar
import io.reactivex.Completable
import io.reactivex.Single

interface ProfileManager: BaseManager {
    fun getProfileAPI(): Single<CoverPicture>
    fun getProfile(): LiveData<ProfileAndAvatar>
    fun getProfileSettings(): Single<ProfileSettings>
    fun getProfileHeader(): Single<ProfileMenuModel>
    fun updateProfile(profileSettings: ProfileSettings): Completable
    fun uploadAvatar(bitmap: Bitmap): Single<String>
    fun changePassword(changePasswordRequest: ChangePasswordRequest): Completable
    fun logout(): Completable
}