package com.android.template.ui.bottom_menu4.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.bottom_menu4.viewmodel.BottomMenu4ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface BottomMenu4Module {
    @Binds
    @IntoMap
    @ViewModelKey(BottomMenu4ViewModel::class)
    fun provideBottomMenu4ViewModel(viewModel: BottomMenu4ViewModel): ViewModel
}