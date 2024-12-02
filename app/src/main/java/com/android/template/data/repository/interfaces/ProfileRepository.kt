package com.android.template.data.repository.interfaces

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.api.model.ProfileMenuModel
import com.android.template.data.models.api.request.ChangePasswordRequest
import com.android.template.data.models.db.ProfileAndAvatar
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ProfileRepository : BaseRepository {
    fun getProfile(): LiveData<ProfileAndAvatar>
    fun getProfileSettings(): Single<ProfileSettings>
    fun getProfileHeader(): Single<ProfileMenuModel>
    fun updateProfile(profileSettings: ProfileSettings): Completable
    fun changePassword(changePasswordRequest: ChangePasswordRequest): Completable
    fun uploadAvatar(bitmap: Bitmap): Single<String>
    fun logout(): Completable
}