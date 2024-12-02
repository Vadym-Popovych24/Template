package com.android.template.data.repository.impl

import com.android.template.data.models.api.response.Article
import com.android.template.data.remote.interfaces.NewsWebservice
import com.android.template.data.repository.interfaces.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val newsWebservice: NewsWebservice
) : NewsRepository {

    override suspend fun getNews(): List<Article> = newsWebservice.getNews().articles

    override fun getProgress(): Flow<Int> = flow {
        var progress = 0
        while (progress < 100) {
            progress += 1
            delay(20)
            emit(progress)
        }
    }.flowOn(dispatcher)
}