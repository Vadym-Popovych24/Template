package com.android.template.data.repository.interfaces

import com.android.template.data.models.api.response.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(): List<Article>

    fun getProgress(): Flow<Int>
}