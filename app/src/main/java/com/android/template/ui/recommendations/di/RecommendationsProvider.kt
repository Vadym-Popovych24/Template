package com.android.template.ui.recommendations.di

import com.android.template.ui.recommendations.RecommendationsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RecommendationsProvider {

    @ContributesAndroidInjector(modules = [RecommendationsModule::class])
    abstract fun provideRecommendationsFragment(): RecommendationsFragment
}