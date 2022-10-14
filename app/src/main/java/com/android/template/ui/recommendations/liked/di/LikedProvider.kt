package com.android.template.ui.recommendations.liked.di

import com.android.template.ui.recommendations.liked.LikedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class LikedProvider {

    @ContributesAndroidInjector(modules = [LikedModule::class])
    abstract fun provideLikedFragment(): LikedFragment
}