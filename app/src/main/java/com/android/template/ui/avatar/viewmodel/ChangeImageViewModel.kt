package com.android.template.ui.avatar.viewmodel

import com.android.template.data.models.enums.ChangeImageType
import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import javax.inject.Inject

class ChangeImageViewModel @Inject constructor(
    private val profileManager: ProfileManager
) : BaseViewModel() {

    private var changeAvatarType = ChangeImageType.UNSPECIFIED

    fun setChangeAvatarType(type: ChangeImageType) {
        changeAvatarType = type
    }

    fun getChangeAvatarType() = changeAvatarType

    fun updateAvatar(avatarPath: String) {
        makeRx(profileManager.updateAvatar(avatarPath)) {}
    }

    fun updateCover(coverPath: String) {
        makeRx(profileManager.updateCover(coverPath)) {}
    }

}