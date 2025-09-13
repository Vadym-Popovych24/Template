package com.android.template.ui.bottom_menu3.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.bottom_menu3.viewmodel.BottomMenu3ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface BottomMenu3Module {

    @Binds
    @IntoMap
    @ViewModelKey(BottomMenu3ViewModel::class)
    fun provideBottomMenu3ViewModel(viewModel: BottomMenu3ViewModel): ViewModel
}