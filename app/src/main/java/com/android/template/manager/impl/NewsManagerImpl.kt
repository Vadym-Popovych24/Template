package com.android.template.manager.impl

import com.android.template.data.models.api.response.Article
import com.android.template.data.repository.interfaces.NewsRepository
import com.android.template.manager.interfaces.NewsManager
import javax.inject.Inject

class NewsManagerImpl @Inject constructor(private val newsRepository: NewsRepository) : NewsManager {

    override suspend fun getNews(): List<Article> = newsRepository.getNews()
}