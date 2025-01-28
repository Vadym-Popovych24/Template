package com.android.template.ui.popular.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.template.manager.interfaces.MovieManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.ui.popular.PopularMovieViewItem
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val movieManager: MovieManager
) : BaseViewModel() {

    private val _item = MutableLiveData<PopularMovieViewItem>()
    val item: LiveData<PopularMovieViewItem> = _item

    private val _details = MutableLiveData<MovieDetailsView?>()
    val details: LiveData<MovieDetailsView?> = _details

    suspend fun getMovieItem(id: Long) {
        movieManager.getMovieFromDB(id).collect { item ->
            _item.value = PopularMovieViewItem.mapFrom(item)
        }
    }

    fun clearLiveData() {
        _details.value = null
    }

    fun getDetails(id: Long) {
        makeRx(movieManager.getMovieDetails(id)) {
            _details.value = MovieDetailsView.mapFrom(it)
        }
    }

}