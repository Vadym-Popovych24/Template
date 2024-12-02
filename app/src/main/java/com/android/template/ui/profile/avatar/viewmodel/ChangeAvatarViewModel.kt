package com.android.template.ui.profile.avatar.viewmodel

import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import javax.inject.Inject

class ChangeAvatarViewModel @Inject constructor(
    private val profileManager: ProfileManager
) :
    BaseViewModel() {

    /*fun changeAvatar(bitmapSingle: Single<Bitmap>, finishCallback: () -> Unit): Nothing = TODO("change to new cropper lib")
        makeRx(bitmapSingle.flatMap {
            profileManager.uploadAvatar(it)
        }) {
            finishCallback()
        }*/

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