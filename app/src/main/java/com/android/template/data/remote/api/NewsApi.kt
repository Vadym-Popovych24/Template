package com.android.template.data.remote.api

import com.android.template.data.models.api.response.NewsResponse
import retrofit2.http.GET

interface NewsApi {

    @GET(ApiEndpoints.ENDPOINT_GET_NEWS)
    suspend fun getNews(): NewsResponse
}