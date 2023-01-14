package com.android.template.ui.coroutine.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.coroutine.viewmodel.CoroutineViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CoroutineModule {
    @Binds
    @IntoMap
    @ViewModelKey(CoroutineViewModel::class)
    fun provideCoroutineViewModel(viewModel: CoroutineViewModel): ViewModel
}