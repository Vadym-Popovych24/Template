package com.android.template.ui.navigation.viewmodel

import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseProfileDataViewModel
import javax.inject.Inject

class NavigationHeaderViewModel @Inject constructor(
    profileManager: ProfileManager
) : BaseProfileDataViewModel(profileManager){

}