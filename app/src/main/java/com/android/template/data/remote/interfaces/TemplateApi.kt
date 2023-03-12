package com.android.template.data.remote.interfaces

import com.android.template.data.models.api.response.NewsResponse
import retrofit2.http.GET

interface TemplateApi {

    @GET("top-headlines?sources=techcrunch&apiKey=3f76680cbec04560bfb536eaf7eb13b6")
    suspend fun getNews(): NewsResponse
}