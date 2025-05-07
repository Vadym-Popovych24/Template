package com.android.template.data.repository.impl

import com.android.template.data.models.api.response.Article
import com.android.template.data.models.api.response.NewsResponse
import com.android.template.data.models.api.response.Source
import com.android.template.data.remote.interfaces.NewsWebservice
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class NewsRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: NewsRepositoryImpl
    private val webservice: NewsWebservice = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = NewsRepositoryImpl(dispatcher, webservice)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getNewsListOfArticlesFromWebservice() = runTest {
        // Given
        val expectedArticles = listOf(
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
            ),
            Article(
                source = Source(
                    id = "2",
                    name = "Source name 2"
                ),
                author = "Author 2",
                title = "Title 2",
                description = "Description 2",
                url = null,
                urlToImage = null,
                publishedAt = "10.04.2025",
                content = null
            )
        )

        coEvery { webservice.getNews() } returns NewsResponse(
            status = "active",
            totalResults = "2",
            articles = expectedArticles
        )

        // When
        val result = repository.getNews()

        // Then
        TestCase.assertEquals(expectedArticles, result)
        coVerify { webservice.getNews() }
    }

    @Test
    fun getProgressEmitsFrom1To100() = runTest {
        // When
        val progressList = repository.getProgress().toList()

        // Then
        TestCase.assertEquals(101, progressList.size)
        TestCase.assertEquals(1, progressList.first())
        TestCase.assertEquals(101, progressList.last())
    }
}