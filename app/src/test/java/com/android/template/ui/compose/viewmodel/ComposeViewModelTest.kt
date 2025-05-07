package com.android.template.ui.compose.viewmodel

import com.android.template.data.models.api.ErrorLiveData
import com.android.template.data.models.api.ResultLiveData
import com.android.template.data.models.api.SuccessLiveData
import com.android.template.data.models.api.response.Article
import com.android.template.data.models.api.response.Source
import com.android.template.manager.interfaces.NewsManager
import com.android.template.testutils.ViewModelTest
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ComposeViewModelTest : ViewModelTest()  {

    private val newsManager: NewsManager = mockk()
    private val viewModel = ComposeViewModel(newsManager)

    @Test
    fun `getNews should emit SuccessLiveData`() = runTest {
        val articles = listOf(
            Article(
                source = Source(
                    id = "1",
                    name = "Source name 1"
                ),
                author = "Author 1",
                title = "Title 1",
                description = "Description 1",
                url = null,
                urlToImage = null,
                publishedAt = "10.04.2025",
                content = null
            )
        )
        coEvery { newsManager.getNews() } returns articles

        val emitted = CompletableDeferred<ResultLiveData<List<Article>>>()

        viewModel.article.observeForever {
            if (it is SuccessLiveData) {
                emitted.complete(it)
            }
        }
        viewModel.getNews()

        val result = emitted.await()
        assertTrue(result is SuccessLiveData)
        assertEquals(articles, (result as SuccessLiveData).value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getNews should emit ErrorLiveData on failure`() = runTest {
        val error = RuntimeException("Failed")
        coEvery { newsManager.getNews() } throws error

        val emitted = CompletableDeferred<ResultLiveData<List<Article>>>()

        viewModel.article.observeForever {
            if (it is ErrorLiveData) {
                emitted.complete(it)
            }
        }
        viewModel.getNews()


        val result = emitted.await()
        assertTrue(result is ErrorLiveData )
        assertEquals("Failed", (result as ErrorLiveData).error.message)
    }

}