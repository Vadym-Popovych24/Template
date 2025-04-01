package com.android.template.data.remote.impl

import com.android.template.data.models.api.response.Article
import com.android.template.data.models.api.response.NewsResponse
import com.android.template.data.models.api.response.Source
import com.android.template.data.remote.api.NewsApi
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit

@RunWith(JUnit4::class)
class NewsWebserviceImplTest {

    private lateinit var newsWebservice: NewsWebserviceImpl
    private val mockNewsRetrofit: Retrofit = mockk()
    private val mockNewsApi: NewsApi = mockk()

    @Before
    fun setup() {

        every { mockNewsRetrofit.create(NewsApi::class.java) } returns mockNewsApi

        newsWebservice = NewsWebserviceImpl(mockNewsRetrofit)
    }

    @Test
    fun testNewsApiIsCorrectlyInitialized() {
        assertNotNull(newsWebservice.newsApi)
        verify { mockNewsRetrofit.create(NewsApi::class.java) } // Verify that create() was called
    }

    @Test
    fun testNewsApiIsInstanceOfNewsApi() {
        assertTrue(newsWebservice.newsApi is NewsApi)
        verify { mockNewsRetrofit.create(NewsApi::class.java) }
    }

    @Test
    fun getNewsReturnsExpectedNewsResponse() = runTest {
        // Given
        val mockResponse = NewsResponse(
            status = "ok",
            totalResults = "1",
            articles = listOf(
                Article(
                    source = Source(id = "1", name = "Test"),
                    author = "author test",
                    title = "Test News",
                    description = "This is a test news article.",
                    url = "https://example.com",
                    urlToImage = "https://example.com/image.jpg",
                    publishedAt = "2025-03-23T12:00:00Z",
                    content = "This is the content of the article."
                )
            )
        )

        // Mock API call
        coEvery { mockNewsApi.getNews() } returns mockResponse

        // When
        val result = newsWebservice.getNews()

        // Then
        assertEquals(mockResponse, result)
        assertEquals("ok", result.status)
        assertEquals(1, result.articles.size)
        assertEquals("Test", result.articles[0].source?.name)
    }

}