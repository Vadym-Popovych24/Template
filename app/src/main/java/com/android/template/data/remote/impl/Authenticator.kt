package com.android.template.data.remote.impl

import com.rx2androidnetworking.Rx2AndroidNetworking
import com.android.template.data.models.api.response.LoginResponse
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.data.remote.interfaces.RefreshTokenWebservice
import com.android.template.utils.AppConstants
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class Authenticator @Inject constructor(private val preferences: PreferencesHelper) :
    RefreshTokenWebservice {

    override fun refreshToken(refreshToken: String): Completable =
        Rx2AndroidNetworking.post(AppConstants.ENDPOINT_SERVER_LOGIN)
            .addHeaders("Content-Type", "application/x-www-form-urlencoded")
            .addBodyParameter("refresh_token", refreshToken)
            .addBodyParameter("grant_type", AppConstants.GRANT_REFRESH_TOKEN)
            .addBodyParameter("client_id", AppConstants.CLIENT_ID)
            .addBodyParameter("client_secret", AppConstants.CLIENT_SECRET)
            .build()
            .getObjectSingle(LoginResponse::class.java).flatMapCompletable {
                Completable.fromAction {
                    it.getToken()?.let { token -> preferences.setToken(token) }
                    it.getRefreshToken()
                        ?.let { refreshToken -> preferences.setRefreshToken(refreshToken) }
                    it.getValidityPeriod()
                        ?.let { validityPeriod -> preferences.setValidityPeriod(validityPeriod) }
                    preferences.setValidityStart(System.currentTimeMillis())
                }
            } 

    @Synchronized
    fun getToken(): String? = Single.just(preferences).map {
        val isCloseToExpiringDate =
            System.currentTimeMillis() - it.getValidityStart() > it.getValidityPeriod() * 1000L
        if (isCloseToExpiringDate) {
            refreshToken(preferences.getRefreshToken().toString())
                .onErrorComplete { throw FailedToRefreshTokenException() }
                .andThen(Single.just(it.getToken()))
                .blockingGet()
        } else {
            it.getToken()
        }
    }.blockingGet()
}