package com.android.template.ui.crash.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.crash.viewmodel.CrashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CrashModule {
    @Binds
    @IntoMap
    @ViewModelKey(CrashViewModel::class)
    fun provideCrashVM(viewModel: CrashViewModel): ViewModel
}