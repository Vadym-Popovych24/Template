package com.android.template.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.android.template.data.models.db.MovieEntity

@Dao
interface MovieDao : BaseDao<MovieEntity> {

    @Transaction
    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun getMovies(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movies")
    fun clearTable()

    @Transaction
    @Query("SELECT * FROM movies WHERE id =:id")
    fun getMovieById(id: Long): LiveData<MovieEntity>
}