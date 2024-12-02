package com.android.template

import android.app.Application
import android.content.Context
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.di.component.AppComponent
import com.android.template.di.component.DaggerAppComponent
import com.google.gson.Gson
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class TemplateApp : Application(), HasAndroidInjector {

    lateinit var component: AppComponent

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var gson: Gson

    @Inject
    lateinit var preferences: PreferencesHelper

    init {
        appContext = this
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        instance = this

        component = DaggerAppComponent.builder()
            .application(this)
            .context(this)
            .build()
        component.inject(this)

        AppCenter.start(this, "1bf327aa-96f7-4b4a-b896-f0784b76f684",
            Analytics::class.java, Crashes::class.java)

    }

    fun getAppVersion(): String = BuildConfig.VERSION_NAME

    companion object {
        lateinit var appContext: Context
            private set

        lateinit var instance: TemplateApp
            private set
    }

}
