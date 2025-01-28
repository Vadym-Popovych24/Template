package com.android.template.data.local.interfaces

import androidx.paging.PagingSource
import com.android.template.data.models.db.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MoviesStorage {

    fun getMovies(): PagingSource<Int, MovieEntity>

    fun saveMovies(movies: List<MovieEntity>)

    fun clearMovie()

    fun getMovieFromDB(id: Long): Flow<MovieEntity>
}