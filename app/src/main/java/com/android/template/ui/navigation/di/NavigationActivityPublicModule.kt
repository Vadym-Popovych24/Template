package com.android.template.ui.navigation.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.navigation.viewmodel.NavigationHeaderViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface NavigationActivityPublicModule {
    @Binds
    @IntoMap
    @ViewModelKey(NavigationHeaderViewModel::class)
    fun provideVM(navigationVM: NavigationHeaderViewModel): ViewModel
}