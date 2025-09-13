package com.android.template.ui.coroutine.di

import com.android.template.ui.coroutine.CoroutineFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class CoroutineProvider {

    @ContributesAndroidInjector(modules = [CoroutineModule::class])
    abstract fun provideCoroutineFragment(): CoroutineFragment
}