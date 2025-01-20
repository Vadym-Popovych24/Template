package com.android.template.ui.profile.di

import com.android.template.ui.profile.ProfileFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileProvider {

    @ContributesAndroidInjector()
    abstract fun providePostsFragment(): ProfileFragment
}