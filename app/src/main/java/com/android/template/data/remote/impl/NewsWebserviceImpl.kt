package com.android.template.data.remote.impl

import com.android.template.data.models.api.response.NewsResponse
import com.android.template.data.remote.api.NewsApi
import com.android.template.data.remote.interfaces.NewsWebservice
import com.android.template.di.qualifiers.NewsRetrofit
import retrofit2.Retrofit
import javax.inject.Inject

class NewsWebserviceImpl @Inject constructor(
    @NewsRetrofit newsRetrofit: Retrofit
) : NewsWebservice {

    private val newsApi = newsRetrofit.create(NewsApi::class.java)

    override suspend fun getNews(): NewsResponse = newsApi.getNews()

}