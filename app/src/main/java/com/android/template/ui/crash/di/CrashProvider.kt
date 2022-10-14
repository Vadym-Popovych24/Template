package com.android.template.ui.crash.di

import com.android.template.ui.crash.CrashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CrashProvider {
    @ContributesAndroidInjector(modules = [CrashModule::class])
    abstract fun provideCrashFragment(): CrashFragment
}