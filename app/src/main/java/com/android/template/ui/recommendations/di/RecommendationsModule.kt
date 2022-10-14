package com.android.template.ui.recommendations.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.recommendations.viewmodel.RecommendationsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface RecommendationsModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecommendationsViewModel::class)
    fun providerRecommendationsViewModel(viewModel: RecommendationsViewModel): ViewModel

}