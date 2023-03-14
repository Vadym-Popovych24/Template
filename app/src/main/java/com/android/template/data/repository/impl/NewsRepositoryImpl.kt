package com.android.template.data.repository.impl

import com.android.template.BuildConfig
import com.android.template.data.models.api.response.Article
import com.android.template.data.remote.interfaces.TemplateApi
import com.android.template.data.repository.interfaces.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(private val dispatcher: CoroutineDispatcher) : NewsRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_SERVER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val templateApi = retrofit.create(TemplateApi::class.java)

    override suspend fun getNews(): List<Article> = templateApi.getNews().articles

    override fun getProgress(): Flow<Int> = flow {
        var progress = 0
        while (progress < 100) {
            progress += 1
            delay(20)
            emit(progress)
        }
    }.flowOn(dispatcher)
}