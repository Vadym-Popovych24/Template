package com.android.template.data.repository.interfaces

import com.android.template.data.models.api.response.Article

interface NewsRepository {

    suspend fun getNews(): List<Article>
}