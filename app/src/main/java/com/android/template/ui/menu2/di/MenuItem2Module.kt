package com.android.template.ui.menu2.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.menu2.viewmodel.MenuItem2ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface MenuItem2Module {
        @Binds
        @IntoMap
        @ViewModelKey(MenuItem2ViewModel::class)
        fun provideMenuItem2ViewModel(viewModel: MenuItem2ViewModel): ViewModel
}