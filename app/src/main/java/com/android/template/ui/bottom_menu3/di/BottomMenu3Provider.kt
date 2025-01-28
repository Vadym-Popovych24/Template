package com.android.template.ui.bottom_menu3.di

import com.android.template.ui.bottom_menu3.BottomMenu3Fragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BottomMenu3Provider {

    @ContributesAndroidInjector(modules = [BottomMenu3Module::class])
    abstract fun provideBottomMenu3Fragment(): BottomMenu3Fragment
}