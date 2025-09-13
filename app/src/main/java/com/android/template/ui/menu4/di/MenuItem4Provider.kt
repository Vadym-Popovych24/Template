package com.android.template.ui.menu4.di

import com.android.template.ui.menu4.MenuItem4Fragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MenuItem4Provider {

    @ContributesAndroidInjector(modules = [MenuItem4Module::class])
    abstract fun provideMenuItem4Fragment(): MenuItem4Fragment
}