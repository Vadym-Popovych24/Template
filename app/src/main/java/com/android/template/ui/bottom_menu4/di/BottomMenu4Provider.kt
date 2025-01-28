package com.android.template.ui.bottom_menu4.di

import com.android.template.ui.bottom_menu4.BottomMenu4Fragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BottomMenu4Provider {

    @ContributesAndroidInjector(modules = [BottomMenu4Module::class])
    abstract fun provideBottomMenu4Fragment(): BottomMenu4Fragment
}