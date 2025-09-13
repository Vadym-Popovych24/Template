package com.android.template.ui.popular.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.template.manager.interfaces.MovieManager
import com.android.template.ui.base.BaseViewModel
import javax.inject.Inject

class MovieDetailsViewModel @Inject constructor(
    private val movieManager: MovieManager
) : BaseViewModel() {

    private val _details = MutableLiveData<MovieDetailsView?>()
    val details: LiveData<MovieDetailsView?> = _details

    fun getMovieItem(id: Long) = movieManager.getMovieFromDB(id)

    fun clearLiveData() {
        _details.value = null
    }

    fun getDetails(id: Long) {
        // Clear the LiveData first
        _details.value = null

        makeRx(movieManager.getMovieDetails(id)) {
            _details.value = MovieDetailsView.mapFrom(it)
        }
    }

}