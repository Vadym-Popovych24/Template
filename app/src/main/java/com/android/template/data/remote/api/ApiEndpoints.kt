package com.android.template.data.remote.api

import com.android.template.BuildConfig

object ApiEndpoints {

    const val BASE_URL = BuildConfig.MOVIE_DB_API_ENDPOINT
    const val WEB_URL = BuildConfig.MOVIE_DB_WEB_ENDPOINT
    const val API_KEY = BuildConfig.API_KEY
    const val NEWS_URL = BuildConfig.NEWS_API_ENDPOINT

    const val ENDPOINT_WEB_APPROVE = "${WEB_URL}authenticate/"
    const val ENDPOINT_REQUEST_TOKEN = "authentication/token/new"
    const val ENDPOINT_APPROVE_REQUEST_TOKEN = "authenticate"
    const val ENDPOINT_CREATE_SESSION = "authentication/session/new"
    const val ENDPOINT_GET_ACCOUNT = "account"

    /**
     * News
     */

    const val ENDPOINT_GET_NEWS = "top-headlines?sources=techcrunch&apiKey=3f76680cbec04560bfb536eaf7eb13b6"

   /**
    * Movies
    */
    const val ENDPOINT_GET_MOVIES = "movie/popular"
    const val ENDPOINT_GET_MOVIE_DETAILS = "movie/{movieId}"


    /**
     * Movie Images
     */
    const val ENDPOINT_MOVIE_IMAGE = "https://image.tmdb.org/t/p/"

    /**
     * For Pagination
     */
    const val PAGINATION_COUNT = 20
}