package com.android.template.ui.compose.di

import com.android.template.ui.compose.ComposeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ComposeProvider {

    @ContributesAndroidInjector(modules = [ComposeModule::class])
    abstract fun provideComposeFragment(): ComposeFragment
}