package com.android.template.ui.popular.di

import com.android.template.ui.popular.PopularFragment
import com.android.template.ui.popular.details.MovieDetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PopularProvider {

    @ContributesAndroidInjector(modules = [PopularModule::class])
    abstract fun providePopularFragment(): PopularFragment


    @ContributesAndroidInjector(modules = [PopularModule::class])
    abstract fun provideMovieDetailsFragment(): MovieDetailsFragment
}