package com.android.template.ui.popular.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.android.template.manager.interfaces.MovieManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.ui.popular.PopularMovieViewItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PopularViewModel @Inject constructor(
    private val movieManager: MovieManager
) : BaseViewModel() {

    val pagingFlow: Flow<PagingData<PopularMovieViewItem>>
        get() {
            return movieManager.moviePagingData().flow.map {
                    it.map {
                        PopularMovieViewItem.mapFrom(it)
                    }
                }.cachedIn(viewModelScope)

        }
}