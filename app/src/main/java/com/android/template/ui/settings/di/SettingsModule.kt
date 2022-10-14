package com.android.template.ui.settings.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.settings.password.viewmodel.ChangePasswordViewModel
import com.android.template.ui.settings.profile.viewmodel.ProfileSettingsViewModel
import com.android.template.ui.settings.viewmodel.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SettingsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun provideSettingsVM(settingsVM: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    fun provideChangePasswordVM(changePasswordVM: ChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileSettingsViewModel::class)
    fun provideProfileSettingsVM(profileSettingsVM: ProfileSettingsViewModel): ViewModel
}