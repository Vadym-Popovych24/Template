package com.android.template.ui.settings.di

import com.android.template.ui.settings.SettingsFragment
import com.android.template.ui.settings.password.ChangePasswordFragment
import com.android.template.ui.settings.profile.ProfileSettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsProvider {

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    abstract fun provideSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    abstract fun provideChangePasswordFragment(): ChangePasswordFragment

    @ContributesAndroidInjector(modules = [SettingsModule::class])
    abstract fun provideProfileSettingsFragment(): ProfileSettingsFragment
}