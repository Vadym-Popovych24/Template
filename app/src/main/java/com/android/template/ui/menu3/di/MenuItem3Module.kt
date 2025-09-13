package com.android.template.ui.menu3.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.menu3.viewmodel.MenuItem3ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MenuItem3Module {
        @Binds
        @IntoMap
        @ViewModelKey(MenuItem3ViewModel::class)
        fun provideMenuItem3ViewModel(viewModel: MenuItem3ViewModel): ViewModel
}