package com.android.template.data.remote.api

import com.android.template.data.models.api.request.SessionRequest
import com.android.template.data.models.api.response.LoginResponse
import com.android.template.data.models.api.response.RequestKeyResponse
import com.android.template.data.models.api.response.SessionResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApi {

    @GET(ApiEndpoints.ENDPOINT_REQUEST_TOKEN)
    fun requestToken(
        @Query("api_key") apiKey: String
    ): Single<RequestKeyResponse>

    @POST(ApiEndpoints.ENDPOINT_CREATE_SESSION)
    fun createSession(
        @Query("api_key") apiKey: String,
        @Body sessionRequest: SessionRequest
    ): Single<SessionResponse>

    @GET("")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Single<LoginResponse>

    @GET("")
    fun signUp(
        @Query("firstName") firstName: String,
        @Query("lastName") lastName: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Single<LoginResponse>
}