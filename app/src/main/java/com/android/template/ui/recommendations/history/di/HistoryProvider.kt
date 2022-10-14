package com.android.template.ui.recommendations.history.di

import com.android.template.ui.recommendations.history.HistoryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HistoryProvider {

    @ContributesAndroidInjector(modules = [HistoryModule::class])
    abstract fun provideHistoryFragment(): HistoryFragment
}