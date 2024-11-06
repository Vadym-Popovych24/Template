package com.android.template.di.modules

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.template.data.local.TemplateDatabase
import com.android.template.data.prefs.AppPreferencesHelper
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.di.qualifiers.PreferenceInfo
import com.android.template.utils.AppConstants
import com.android.template.utils.Connectivity
import com.android.template.utils.ViewModelProviderFactory
import com.android.template.utils.interceptors.AuthDataInterceptor
import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton


@Module
class GeneralModule {
    @Provides
    @Singleton
    fun provideHttpClientWithBasicLogger(preferences: PreferencesHelper): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .addInterceptor(AuthDataInterceptor(preferences)).readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).connectTimeout(10, TimeUnit.SECONDS).build()

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.S")
            .create()
    }

    @Provides
    @Singleton
    fun provideGeocoder(context: Context) = Geocoder(context)

    @Provides
    @Singleton
    internal fun provideTemplateDatabase(context: Context): TemplateDatabase {
        Log.d("myLogs", "Context = " + context::class.java.name)
        return TemplateDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideCacheDir(context: Context) = context.cacheDir

    @Provides
    @Singleton
    fun provideConnectivityUtil(): Connectivity {
        return Connectivity.getInstance()!!
    }

    @Provides
    @PreferenceInfo
    internal fun providePreferenceName(): String {
        return AppConstants.PREF_NAME
    }

    @Provides
    @Singleton
    internal fun providePreferencesHelper(appPreferencesHelper: AppPreferencesHelper): PreferencesHelper {
        return appPreferencesHelper
    }

    @Provides
    fun provideViewModelFactory(providerMap: MutableMap<Class<out ViewModel>, Provider<ViewModel>>): ViewModelProviderFactory {
        return ViewModelProviderFactory(providerMap)
    }

    @Provides
    @Singleton
    internal fun provideDispatchersDefault(): CoroutineDispatcher {
        return Dispatchers.Default
    }

}