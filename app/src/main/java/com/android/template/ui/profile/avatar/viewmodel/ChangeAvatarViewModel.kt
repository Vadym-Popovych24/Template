package com.android.template.ui.profile.avatar.viewmodel

import android.graphics.Bitmap
import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import io.reactivex.Single
import javax.inject.Inject

class ChangeAvatarViewModel @Inject constructor(
    private val profileManager: ProfileManager
) :
    BaseViewModel() {

    fun changeAvatar(bitmapSingle: Single<Bitmap>, finishCallback: () -> Unit) =
        makeRx(bitmapSingle.flatMap {
            profileManager.uploadAvatar(it)
        }) {
            finishCallback()
        }

    fun updateProfileDate() = makeRx(profileManager.getProfileAPI()){}


/*    fun changeResumeAvatar(
        bitmapSingle: Single<Bitmap>,
        resumeId: String,
        finishCallback: () -> Unit
    ) = makeRx(bitmapSingle.flatMap {
        resumeManager.uploadResumeAvatar(resumeId, it)
    }) {
        finishCallback()
    }*/

}