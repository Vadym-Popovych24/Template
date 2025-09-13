package com.android.template.ui.bottom_menu2.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.bottom_menu2.viewmodel.BottomMenu2ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface BottomMenu2Module {

    @Binds
    @IntoMap
    @ViewModelKey(BottomMenu2ViewModel::class)
    fun provideBottomMenu2ViewModel(viewModel: BottomMenu2ViewModel): ViewModel
}