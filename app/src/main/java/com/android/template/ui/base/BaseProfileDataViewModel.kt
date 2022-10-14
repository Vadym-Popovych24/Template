package com.android.template.ui.base

import androidx.databinding.ObservableField
import com.android.template.data.models.api.model.CoverPicture
import com.android.template.manager.interfaces.ProfileManager
import javax.inject.Inject

open class BaseProfileDataViewModel @Inject constructor(private val profileManager: ProfileManager) :
        BaseViewModel() {

    var moveToProfileCallback: (() -> Unit)? = null
    val userName = ObservableField<String>()
    val userEmail = ObservableField<String>()
    val userAvatar = ObservableField<String>()

    init {
        getProfileDataFromPrefs()
    }

    private fun getProfileDataFromPrefs() = makeRx(profileManager.getProfileHeader()) {
        userName.set(it.userName)
        userEmail.set(it.email)
        userAvatar.set(it.userAvatar)
    }

    fun loadProfileData(callback: ((CoverPicture) -> Unit)) = makeRx(profileManager.getProfileAPI(), callback)

    fun getProfileDataFromDB() = profileManager.getProfile()

    fun moveToProfileClick() = moveToProfileCallback?.invoke()


}