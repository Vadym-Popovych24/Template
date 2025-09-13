package com.android.template.data.local.interfaces

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import com.android.template.data.models.db.MovieEntity

interface MoviesStorage {

    fun getMovies(): PagingSource<Int, MovieEntity>

    fun saveMovies(movies: List<MovieEntity>)

    fun saveMovie(movie: MovieEntity)

    fun clearMovie()

    fun getMovieFromDB(id: Long): LiveData<MovieEntity>
}