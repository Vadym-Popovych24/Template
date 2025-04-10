package com.android.template.manager.impl

import com.android.template.data.models.api.response.Article
import com.android.template.data.models.api.response.Source
import com.android.template.data.repository.interfaces.NewsRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class NewsManagerImplTest {

    private val dispatcher = StandardTestDispatcher()
    private val newsRepository: NewsRepository = mockk()
    private lateinit var newsManager: NewsManagerImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        newsManager = NewsManagerImpl(newsRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getNewsDelegatesToRepository() = runTest {
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

        coEvery { newsRepository.getNews() } returns expectedArticles

        // When
        val result = newsManager.getNews()

        // Then
        assertEquals(expectedArticles, result)
        coVerify { newsRepository.getNews() }
    }

    @Test
    fun getProgressDelegatesToRepository() = runTest {
        // Given
        val progressFlow = flowOf(10, 20, 30)
        every { newsRepository.getProgress() } returns progressFlow

        // When
        val result = newsManager.getProgress().toList()

        // Then
        assertEquals(listOf(10, 20, 30), result)
        verify { newsRepository.getProgress() }
    }
}