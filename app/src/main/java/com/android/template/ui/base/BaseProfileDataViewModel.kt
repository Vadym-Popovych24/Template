package com.android.template.ui.base

import androidx.databinding.ObservableField
import com.android.template.manager.interfaces.ProfileManager
import javax.inject.Inject

open class BaseProfileDataViewModel @Inject constructor(private val profileManager: ProfileManager) :
    BaseViewModel() {

    var moveToProfileCallback: (() -> Unit)? = null
    val userName = ObservableField<String>()
    val userEmail = ObservableField<String>()
    val userAvatar = ObservableField<String>()

    fun getProfileDataFromDB() = profileManager.getProfile()

    fun moveToProfileClick() = moveToProfileCallback?.invoke()

}