package com.android.template.data.remote.interfaces

import com.android.template.data.models.api.response.NewsResponse

interface NewsWebservice {

    suspend fun getNews(): NewsResponse
}