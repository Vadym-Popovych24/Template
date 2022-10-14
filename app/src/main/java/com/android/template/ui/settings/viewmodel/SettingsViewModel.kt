package com.android.template.ui.settings.viewmodel

import androidx.databinding.ObservableField
import com.android.template.TemplateApp
import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val profileManager: ProfileManager) : BaseViewModel() {

    val appVersion = ObservableField<String>(TemplateApp.instance.getAppVersion())

    fun logOut(callback: () -> Unit) = makeRx(profileManager.logout().onErrorComplete(), callback)

}