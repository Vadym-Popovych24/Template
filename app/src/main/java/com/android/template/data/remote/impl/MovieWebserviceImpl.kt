package com.android.template.data.remote.impl

import com.android.template.data.models.api.response.Movie
import com.android.template.data.models.api.response.MovieDetailsResponse
import com.android.template.data.remote.api.MovieApi
import com.android.template.data.remote.interfaces.MovieWebservice
import com.android.template.di.qualifiers.BaseRetrofit
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import javax.inject.Inject

class MovieWebserviceImpl @Inject constructor(@BaseRetrofit baseRetrofit: Retrofit) : MovieWebservice {

    private val movieApi = baseRetrofit.create(MovieApi::class.java)

    override fun getMovies(page: String, language: String): Single<List<Movie>> =
        movieApi.getMovies(page, language).map {
            it.results
        }

    override fun getMovieDetails(id: Long): Single<MovieDetailsResponse> =
        movieApi.getMovieDetails(id)

}