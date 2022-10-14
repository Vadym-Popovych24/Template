package com.android.template.ui.splash.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.splash.viewmodel.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SplashModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun provideVM(loginVM: SplashViewModel): ViewModel
}