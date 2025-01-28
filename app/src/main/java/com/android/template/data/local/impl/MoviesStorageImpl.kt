package com.android.template.data.local.impl

import androidx.paging.PagingSource
import com.android.template.data.local.TemplateDatabase
import com.android.template.data.local.interfaces.MoviesStorage
import com.android.template.data.models.db.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesStorageImpl @Inject constructor(private val database: TemplateDatabase) :
    MoviesStorage {

    override fun getMovies(): PagingSource<Int, MovieEntity> =
        database.movieDao().getMovies()

    override fun saveMovies(movies: List<MovieEntity>) {
        database.movieDao().insertAll(movies)
    }

    override fun clearMovie() {
        database.movieDao().clearTable()
    }

    override fun getMovieFromDB(id: Long): Flow<MovieEntity> =
        database.movieDao().getMovieById(id)

}