package com.android.template.di.modules

import com.android.template.manager.impl.*
import com.android.template.manager.interfaces.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ManagerModule {
    @Binds
    @Singleton
    fun provideLoginManager(manager: LoginManagerImpl): LoginManager

    @Binds
    @Singleton
    fun provideProfileManager(manager: ProfileManagerImpl): ProfileManager

    @Binds
    @Singleton
    fun provideNewsManager(manager: NewsManagerImpl): NewsManager

    @Binds
    @Singleton
    fun provideMovieManager(manager: MovieManagerImpl): MovieManager
}