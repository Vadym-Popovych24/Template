package com.android.template.data.repository.impl

import com.android.template.BuildConfig
import com.android.template.data.models.api.response.Article
import com.android.template.data.remote.interfaces.TemplateApi
import com.android.template.data.repository.interfaces.NewsRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor() : NewsRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_SERVER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val templateApi = retrofit.create(TemplateApi::class.java)

    override suspend fun getNews(): List<Article> = templateApi.getNews().articles
}