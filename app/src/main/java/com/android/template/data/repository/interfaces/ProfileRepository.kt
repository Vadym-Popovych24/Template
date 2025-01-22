package com.android.template.data.repository.interfaces

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.db.ProfileAndAvatar
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface ProfileRepository : BaseRepository {
    fun getProfile(): LiveData<ProfileAndAvatar>
    fun getProfileSettings(): Single<ProfileSettings>
    fun updateProfile(profileSettings: ProfileSettings): Completable
    fun changePassword(oldPassword: String, newPassword: String): Single<Int>
    fun updateAvatar(avatarPath: String): Single<Int>
    fun updateCover(coverPath: String): Single<Int>
    fun uploadAvatar(bitmap: Bitmap): Single<String>
    fun logout(): Completable
}