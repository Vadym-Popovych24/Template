package com.android.template.data.remote.interfaces

import com.android.template.data.models.api.response.Movie
import com.android.template.data.models.api.response.MovieDetailsResponse
import io.reactivex.rxjava3.core.Single

interface MovieWebservice {

    fun getMovies(page: String, language: String): Single<List<Movie>>

    fun getMovieDetails(id: Long): Single<MovieDetailsResponse>
}