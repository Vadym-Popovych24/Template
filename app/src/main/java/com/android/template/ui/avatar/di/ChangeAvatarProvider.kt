package com.android.template.ui.avatar.di

import com.android.template.ui.avatar.ChangeImageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ChangeAvatarProvider {

    @ContributesAndroidInjector(modules = [ChangeAvatarModule::class])
    abstract fun provideChangeAvatarFragment(): ChangeImageFragment
}