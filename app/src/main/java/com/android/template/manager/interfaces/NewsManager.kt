package com.android.template.manager.interfaces

import com.android.template.data.models.api.response.Article
import kotlinx.coroutines.flow.Flow

interface NewsManager {

    suspend fun getNews(): List<Article>

    fun getProgress(): Flow<Int>
}