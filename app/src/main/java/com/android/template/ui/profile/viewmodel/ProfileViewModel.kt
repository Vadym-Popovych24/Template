package com.android.template.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import com.android.template.data.models.db.ProfileAndAvatar
import com.android.template.data.models.enums.ChangeImageType
import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val profileManager: ProfileManager
) : BaseViewModel() {
    private var changeAvatarType = ChangeImageType.UNSPECIFIED

    fun setChangeAvatarType(type: ChangeImageType) {
        changeAvatarType = type
    }

    fun getChangeAvatarType() = changeAvatarType

    fun getProfile(): LiveData<ProfileAndAvatar> =
        profileManager.getProfile()

}