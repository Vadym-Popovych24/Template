package com.android.template.di.modules

import com.android.template.service.NotificationService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceModule {

    @ContributesAndroidInjector()
    internal abstract fun bindNotificationService(): NotificationService
}