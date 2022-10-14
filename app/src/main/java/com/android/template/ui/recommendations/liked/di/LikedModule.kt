package com.android.template.ui.recommendations.liked.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.recommendations.liked.viewmodel.LikedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface LikedModule {

    @Binds
    @IntoMap
    @ViewModelKey(LikedViewModel::class)
    fun provideLikedViewModel(viewModel: LikedViewModel): ViewModel
}