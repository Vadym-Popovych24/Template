package com.android.template.manager.interfaces

import com.android.template.data.models.api.response.Article

interface NewsManager {

    suspend fun getNews(): List<Article>
}