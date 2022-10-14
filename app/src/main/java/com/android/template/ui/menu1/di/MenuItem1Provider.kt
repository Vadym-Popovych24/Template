package com.android.template.ui.menu1.di

import com.android.template.ui.menu1.MenuItem1Fragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MenuItem1Provider {

    @ContributesAndroidInjector(modules = [MenuItem1Module::class])
    abstract fun provideMenuItem1Fragment(): MenuItem1Fragment
}