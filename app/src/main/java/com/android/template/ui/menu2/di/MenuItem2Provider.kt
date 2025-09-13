package com.android.template.ui.menu2.di

import com.android.template.ui.menu2.MenuItem2Fragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MenuItem2Provider {

    @ContributesAndroidInjector(modules = [MenuItem2Module::class])
    abstract fun provideMenuItem2Fragment(): MenuItem2Fragment
}