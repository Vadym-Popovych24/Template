package com.android.template.ui.splash.viewmodel

import androidx.databinding.ObservableInt
import com.android.template.data.models.enums.LoggedInMode
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.ui.base.BaseViewModel
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val mPreferencesHelper: PreferencesHelper) : BaseViewModel() {
    val loggedInMode = ObservableInt(-1)

    internal fun decideNextActivity() {
        when {
            !mPreferencesHelper.getRequestToken().isNullOrEmpty() ->
                loggedInMode.set(LoggedInMode.LOGGED_IN_MODE_SERVER.getType())
            else -> loggedInMode.set(LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType())
        }
    }

}