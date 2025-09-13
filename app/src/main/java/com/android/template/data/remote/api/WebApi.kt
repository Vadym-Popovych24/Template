package com.android.template.data.remote.api

import io.reactivex.rxjava3.core.Completable
import retrofit2.http.GET
import retrofit2.http.Path

interface WebApi {

    @GET("${ApiEndpoints.ENDPOINT_APPROVE_REQUEST_TOKEN}/{requestToken}")
    fun approveRequestToken(@Path("requestToken") requestToken: String): Completable
}