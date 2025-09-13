package com.android.template.ui.menu3.di

import com.android.template.ui.menu3.MenuItem3Fragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MenuItem3Provider {

    @ContributesAndroidInjector(modules = [MenuItem3Module::class])
    abstract fun provideMenuItem3Fragment(): MenuItem3Fragment
}