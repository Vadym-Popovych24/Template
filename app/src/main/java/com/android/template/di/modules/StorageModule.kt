package com.android.template.di.modules

import com.android.template.data.local.impl.*
import com.android.template.data.local.interfaces.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface StorageModule {

    @Binds
    @Singleton
    fun provideProfileStorage(storage: ProfileStorageImpl): ProfileStorage
}