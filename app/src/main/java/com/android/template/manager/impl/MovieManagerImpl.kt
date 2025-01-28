package com.android.template.manager.impl

import androidx.paging.Pager
import com.android.template.data.models.api.response.MovieDetailsResponse
import com.android.template.data.models.db.MovieEntity
import com.android.template.data.repository.interfaces.MovieRepository
import com.android.template.manager.interfaces.MovieManager
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieManagerImpl @Inject constructor(private val repository: MovieRepository) :
    BaseManagerImpl(repository), MovieManager {

    override fun moviePagingData(): Pager<Int, MovieEntity> =
        repository.moviePagingData()

    override suspend fun getMovieFromDB(id: Long): Flow<MovieEntity> =
        repository.getMovieFromDB(id)

    override fun getMovieDetails(id: Long): Single<MovieDetailsResponse> =
        repository.getMovieDetails(id)
}