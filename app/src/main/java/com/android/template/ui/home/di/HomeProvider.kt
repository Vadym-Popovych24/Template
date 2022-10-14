package com.android.template.ui.home.di

import com.android.template.ui.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeProvider {

    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun provideHomeFragment(): HomeFragment
}