package com.android.template.ui.menu4.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.menu4.viewmodel.MenuItem4ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MenuItem4Module {
        @Binds
        @IntoMap
        @ViewModelKey(MenuItem4ViewModel::class)
        fun provideMenuItem4ViewModel(viewModel: MenuItem4ViewModel): ViewModel
}