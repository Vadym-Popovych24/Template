package com.android.template.ui.compose.viewmodel

import com.android.template.data.models.api.PendingLiveData
import com.android.template.data.models.api.response.Article
import com.android.template.manager.interfaces.NewsManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.ui.base.LiveResult
import com.android.template.ui.base.MutableLiveResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComposeViewModel @Inject constructor(private val newsManager: NewsManager) : BaseViewModel() {

    private val _article = MutableLiveResult<List<Article>>(PendingLiveData())
    val article: LiveResult<List<Article>> = _article

    fun numbersFlow(): Flow<Int> = newsManager.getProgress()

    fun getNews() = into(_article) { newsManager.getNews() }

}