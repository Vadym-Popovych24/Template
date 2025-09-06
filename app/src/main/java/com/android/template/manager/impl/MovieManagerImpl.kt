package com.android.template.manager.impl

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import com.android.template.data.models.api.response.MovieDetailsResponse
import com.android.template.data.models.db.MovieEntity
import com.android.template.data.repository.interfaces.MovieRepository
import com.android.template.manager.interfaces.MovieManager
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MovieManagerImpl @Inject constructor(private val repository: MovieRepository) :
    BaseManagerImpl(repository), MovieManager {

    override fun moviePagingData(): Pager<Int, MovieEntity> =
        repository.moviePagingData()

    override fun getMovieFromDB(id: Long): LiveData<MovieEntity> =
        repository.getMovieFromDB(id)

    override fun getMovieDetails(id: Long): Single<MovieDetailsResponse> =
        repository.getMovieDetails(id)
}