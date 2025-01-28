package com.android.template.di.modules

import com.android.template.ui.avatar.di.ChangeAvatarProvider
import com.android.template.ui.compose.di.ComposeProvider
import com.android.template.ui.coroutine.di.CoroutineProvider
import com.android.template.ui.crash.CrashActivity
import com.android.template.ui.crash.di.CrashModule
import com.android.template.ui.crash.di.CrashProvider
import com.android.template.ui.bottom_menu4.di.BottomMenu4Provider
import com.android.template.ui.login.LoginActivity
import com.android.template.ui.login.di.LoginModule
import com.android.template.ui.login.di.LoginProvider
import com.android.template.ui.main.MainActivity
import com.android.template.ui.main.di.MainModule
import com.android.template.ui.menu1.di.MenuItem1Provider
import com.android.template.ui.menu2.di.MenuItem2Provider
import com.android.template.ui.menu3.di.MenuItem3Provider
import com.android.template.ui.menu4.di.MenuItem4Provider
import com.android.template.ui.navigation.NavigationActivity
import com.android.template.ui.navigation.di.NavigationActivityPublicModule
import com.android.template.ui.profile.di.ProfileModule
import com.android.template.ui.profile.di.ProfileProvider
import com.android.template.ui.popular.di.PopularProvider
import com.android.template.ui.bottom_menu3.di.BottomMenu3Provider
import com.android.template.ui.bottom_menu2.di.BottomMenu2Provider
import com.android.template.ui.settings.di.SettingsProvider
import com.android.template.ui.splash.SplashActivity
import com.android.template.ui.splash.di.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [SplashModule::class])
    internal abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [LoginModule::class, LoginProvider::class])
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [CrashModule::class, CrashProvider::class])
    internal abstract fun bindCrashActivity(): CrashActivity


    @ContributesAndroidInjector(
        modules = [NavigationActivityPublicModule::class,
            ProfileModule::class,
            ProfileProvider::class,
            ChangeAvatarProvider::class,
            SettingsProvider::class,
            PopularProvider::class,
            BottomMenu2Provider::class,
            BottomMenu3Provider::class,
            BottomMenu4Provider::class,
            MenuItem1Provider::class,
            MenuItem2Provider::class,
            MenuItem3Provider::class,
            MenuItem4Provider::class,
            CoroutineProvider::class,
            ComposeProvider::class
        ]
    )
    internal abstract fun bindNavigationPublicActivity(): NavigationActivity
}