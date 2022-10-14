package com.android.template.ui.menu1.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.menu1.viewmodel.MenuItem1ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MenuItem1Module {
        @Binds
        @IntoMap
        @ViewModelKey(MenuItem1ViewModel::class)
        fun provideMenuItem1ViewModel(viewModel: MenuItem1ViewModel): ViewModel
}