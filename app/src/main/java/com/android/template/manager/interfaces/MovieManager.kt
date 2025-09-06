package com.android.template.manager.interfaces

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import com.android.template.data.models.api.response.MovieDetailsResponse
import com.android.template.data.models.db.MovieEntity
import io.reactivex.rxjava3.core.Single

interface MovieManager : BaseManager {

    fun moviePagingData(): Pager<Int, MovieEntity>

    fun getMovieFromDB(id: Long): LiveData<MovieEntity>

    fun getMovieDetails(id: Long): Single<MovieDetailsResponse>
}