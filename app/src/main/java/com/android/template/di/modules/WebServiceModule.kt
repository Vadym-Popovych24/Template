package com.android.template.di.modules

import com.android.template.data.remote.interfaces.*
import com.android.template.data.remote.impl.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface WebServiceModule {
    @Binds
    @Singleton
    fun provideLoginWebservice(webservice: LoginWebserviceImpl): LoginWebservice

    @Binds
    @Singleton
    fun provideRemoteFileWebservice(webservice: RemoteFileWebserviceImpl): RemoteFileWebservice

    @Binds
    @Singleton
    fun provideProfileWebservice(webservice: ProfileWebserviceImpl): ProfileWebservice

    @Binds
    @Singleton
    fun provideNewsWebservice(webservice: NewsWebserviceImpl): NewsWebservice
}