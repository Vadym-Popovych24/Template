package com.android.template.di.modules

import com.android.template.data.repository.impl.*
import com.android.template.data.repository.interfaces.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideLoginRepository(repository: LoginRepositoryImpl): LoginRepository

    @Binds
    @Singleton
    fun provideProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    fun provideNewsRepository(repository: NewsRepositoryImpl): NewsRepository

    @Binds
    @Singleton
    fun provideMovieRepository(repository: MovieRepositoryImpl): MovieRepository
}