package com.android.template.ui.popular.di

import androidx.lifecycle.ViewModel
import com.android.template.di.qualifiers.ViewModelKey
import com.android.template.ui.popular.details.MovieDetailsViewModel
import com.android.template.ui.popular.viewmodel.PopularViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface PopularModule {
    @Binds
    @IntoMap
    @ViewModelKey(PopularViewModel::class)
    fun providerPopularViewModel(viewModel: PopularViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    fun providerMovieDetailsViewModel(viewModel: MovieDetailsViewModel): ViewModel

}