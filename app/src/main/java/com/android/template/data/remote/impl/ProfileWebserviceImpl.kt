package com.android.template.data.remote.impl

import com.android.template.data.models.api.model.AccountWithSession
import com.android.template.data.models.api.model.ProfileModel
import com.android.template.data.remote.api.AccountApi
import com.android.template.data.remote.api.ApiEndpoints
import com.android.template.data.remote.interfaces.ProfileWebservice
import com.android.template.di.qualifiers.BaseRetrofit
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import javax.inject.Inject

class ProfileWebserviceImpl @Inject constructor(
    @BaseRetrofit baseRetrofit: Retrofit
) : ProfileWebservice {

    private val accountApi = baseRetrofit.create(AccountApi::class.java)

    override fun getAccount(sessionId: String): Single<AccountWithSession> =
        accountApi.getAccount(ApiEndpoints.API_KEY, sessionId).map {
            AccountWithSession(it, sessionId)
        }

    override fun getProfileInfo(profileId: String): Single<ProfileModel> {
        TODO("Not yet implemented")
    }
}