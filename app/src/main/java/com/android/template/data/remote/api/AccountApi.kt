package com.android.template.data.remote.api

import com.android.template.data.models.api.response.AccountResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AccountApi {

    @GET(ApiEndpoints.ENDPOINT_GET_ACCOUNT)
    fun getAccount(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ): Single<AccountResponse>
}