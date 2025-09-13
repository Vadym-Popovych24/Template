package com.android.template.data.remote.api

import com.android.template.data.models.api.response.MovieDetailsResponse
import com.android.template.data.models.api.response.MovieResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET(ApiEndpoints.ENDPOINT_GET_MOVIES)
    fun getMovies(
        @Query("page") page: String,
        @Query("language") language: String
    ): Single<MovieResponse>

    @GET(ApiEndpoints.ENDPOINT_GET_MOVIE_DETAILS)
    fun getMovieDetails(@Path("movieId") id: Long): Single<MovieDetailsResponse>
}