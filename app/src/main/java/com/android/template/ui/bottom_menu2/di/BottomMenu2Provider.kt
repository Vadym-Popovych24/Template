package com.android.template.ui.bottom_menu2.di

import com.android.template.ui.bottom_menu2.BottomMenu2Fragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BottomMenu2Provider {

    @ContributesAndroidInjector(modules = [BottomMenu2Module::class])
    abstract fun provideBottomMenu2Fragment(): BottomMenu2Fragment
}