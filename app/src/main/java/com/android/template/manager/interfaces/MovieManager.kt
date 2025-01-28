package com.android.template.manager.interfaces

import androidx.paging.Pager
import com.android.template.data.models.api.response.MovieDetailsResponse
import com.android.template.data.models.db.MovieEntity
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.flow.Flow

interface MovieManager : BaseManager {

    fun moviePagingData(): Pager<Int, MovieEntity>

    suspend fun getMovieFromDB(id: Long): Flow<MovieEntity>

    fun getMovieDetails(id: Long): Single<MovieDetailsResponse>
}