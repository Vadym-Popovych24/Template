package com.android.template.data.repository.impl

import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.android.template.data.models.api.model.UserModel
import com.android.template.data.models.api.request.DeviceLoginRequest
import com.android.template.data.models.api.request.ServerLoginRequest
import com.android.template.data.models.api.response.LoginResponse
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.data.remote.interfaces.LoginWebservice
import com.android.template.data.remote.interfaces.ProfileWebservice
import com.android.template.data.repository.interfaces.LoginRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.io.UnsupportedEncodingException
import java.util.*
import javax.inject.Inject
import kotlin.jvm.Throws

class LoginRepositoryImpl @Inject constructor(
    private val loginWebservice: LoginWebservice,
    private val profileWebservice: ProfileWebservice,
    private val preferences: PreferencesHelper
) : BaseRepositoryImpl(), LoginRepository {

    override fun signUp(serverLoginRequest: ServerLoginRequest): Completable =
        loginWebservice.signUp(serverLoginRequest)

    override fun getCachedEmail(): Single<String> = Single.just(preferences).map {
        preferences.getEmail() ?: ""
    }

    override fun requestResetPasswordCode(email: String): Completable =
        loginWebservice.requestResetPasswordCode(email)

    override fun sendResetPasswordCode(email: String, code: String): Completable =
        loginWebservice.sendResetPasswordCode(email, code)

    override fun resetPassword(email: String, code: String, password: String): Completable =
        loginWebservice.resetPassword(email, code, password).saveAuthData()

    override fun auth(username: String, password: String): Completable =
        loginWebservice.loginApiCall(username, password).saveAuthData()

    private fun Single<LoginResponse>.saveAuthData() = flatMap { response ->
        response.getToken()?.let { token -> preferences.setToken(token) }
        response.getRefreshToken()
            ?.let { refreshToken -> preferences.setRefreshToken(refreshToken) }
        response.getValidityPeriod()
            ?.let { validityPeriod -> preferences.setValidityPeriod(validityPeriod) }
        preferences.setValidityStart(System.currentTimeMillis())

//        initUUID()

        loginWebservice.loginDevice(
            DeviceLoginRequest(
                preferences.getUUID(),
                preferences.getFCMToken()
            ), response.getToken().toString()
        ).andThen(Single.just(response))
    }.flatMapCompletable {
        val jwt = it.getToken().toString().substring("Bearer ".length)
        decodeAndSave(jwt)
    }

    private fun initUUID() {
        val uuid = UUID.randomUUID().toString()
        preferences.setUUID(uuid)
    }

    @Throws(Exception::class)
    private fun decodeAndSave(JWTEncoded: String): Completable {
        try {
            val split =
                JWTEncoded.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val dataAboutUser = getJson(split[1])
            val parseDate = JsonParser().parse(dataAboutUser)
            val user: UserModel = Gson().fromJson(parseDate, UserModel::class.java)

            val firstName = user.firstName
            val lastName = user.lastName
            Log.d("myLogs", dataAboutUser)
            return setInPrefUserDate(
                "$firstName $lastName",
                user.email,
                user.avatarUrl,
                user.profileId
            )
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return Completable.fromSingle(Single.just(true))
        }
    }

    private fun setInPrefUserDate(
        userName: String,
        email: String,
        avatarUrl: String,
        profileId: Int
    ) = profileWebservice.getProfileInfo(profileId.toString())
        .flatMapCompletable {
            Completable.fromAction {
                preferences.setUserName(userName)
                preferences.setUserAvatar(avatarUrl)
                preferences.setEmail(email)
                preferences.setProfileId(profileId)
                preferences.setLanguageCode(it.culture)
            }
        }

    private fun getJson(strEncoded: String): String {
        val decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes, charset("UTF-8"))
    }

}