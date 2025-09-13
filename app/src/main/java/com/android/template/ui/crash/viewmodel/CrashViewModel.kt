package com.android.template.ui.crash.viewmodel

import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import javax.inject.Inject

class CrashViewModel @Inject constructor(private val profileManager: ProfileManager)  :  BaseViewModel() {

    fun logOut(callback: () -> Unit) = makeRx(profileManager.logout(), callback)

}