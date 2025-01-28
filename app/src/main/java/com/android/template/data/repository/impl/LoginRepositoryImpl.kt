package com.android.template.data.repository.impl

import android.util.Base64
import android.util.Log
import com.android.template.data.local.interfaces.ProfilesStorage
import com.android.template.data.models.api.model.SignUpProfileData
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.android.template.data.models.api.model.UserModel
import com.android.template.data.models.api.response.LoginResponse
import com.android.template.data.models.api.response.RequestKeyResponse
import com.android.template.data.models.db.ProfileEntity
import com.android.template.data.models.exception.SignInException
import com.android.template.data.models.exception.UserAlreadyExistException
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.data.remote.interfaces.LoginWebservice
import com.android.template.data.remote.interfaces.ProfileWebservice
import com.android.template.data.repository.interfaces.LoginRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.io.UnsupportedEncodingException
import java.util.*
import javax.inject.Inject
import kotlin.jvm.Throws

class LoginRepositoryImpl @Inject constructor(
    private val loginWebservice: LoginWebservice,
    private val profileWebservice: ProfileWebservice,
    private val preferences: PreferencesHelper,
    private val profilesStorage: ProfilesStorage
) : BaseRepositoryImpl(), LoginRepository {

    override fun requestToken(email: String): Single<RequestKeyResponse> =
        profilesStorage.getProfileByEmailIgnoreEmpty(email)
            .flatMap { profileEntity ->
                if (profileEntity.email == email) {
                    Single.error(UserAlreadyExistException())
                } else {
                    loginWebservice.requestToken()
                }
            }


    override fun authenticateAccount(
        requestToken: String, signUpProfileData: SignUpProfileData
    ): Completable =
        loginWebservice.approveRequestToken(requestToken)
            .andThen(loginWebservice.createSession(requestToken)
                .flatMap { profileWebservice.getAccount(it.sessionId) })
            .flatMapCompletable { accountResponseWithSession ->
                Completable.fromAction {
                    ProfileEntity.mapTo(
                        requestToken = requestToken,
                        accountWithSession = accountResponseWithSession,
                        signUpProfileData = signUpProfileData
                    ).let { newProfileEntity ->
                        profilesStorage.insertProfile(newProfileEntity)
                        val id = profilesStorage.getProfileIdByEmail(signUpProfileData.email)
                        savePreferences(
                            requestToken = requestToken,
                            email = signUpProfileData.email,
                            userName = "${signUpProfileData.firstName} ${signUpProfileData.lastName}",
                            avatarPath = accountResponseWithSession.accountResponse.avatar.tmdb.avatarPath,
                            id = id
                        )
                    }
                }
            }

    private fun savePreferences(
        requestToken: String,
        email: String,
        userName: String,
        avatarPath: String?,
        id: Long
    ) {
        preferences.setRequestToken(requestToken)
        preferences.setEmail(email)
        preferences.setUserName(userName)
        preferences.setUserAvatar(avatarPath)
        preferences.setDBProfileId(id)
    }

    override fun authByDB(email: String, password: String): Completable =
        profilesStorage.getProfileByEmail(email).flatMapCompletable { profileEntity ->
            if (profileEntity.email == email && profileEntity.password == password) {
                Completable.fromAction {
                    val id = profilesStorage.getProfileIdByEmail(email)
                    savePreferences(
                        requestToken = profileEntity.requestToken,
                        email = profileEntity.email,
                        userName = "${profileEntity.firstName} ${profileEntity.lastName}",
                        avatarPath = profileEntity.avatarPath,
                        id = id
                    )
                }
            } else {
                Completable.error(SignInException())
            }
        }

    // Use for Login by API
    override fun auth(email: String, password: String): Completable =
        loginWebservice.login(email, password).saveAuthData()

    // Use for Sign Up by API
    override fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): Completable = loginWebservice.signUp(firstName, lastName, email, password).saveAuthData()

    private fun Single<LoginResponse>.saveAuthData() = flatMap { response ->
        preferences.setToken(response.accessToken)
        preferences.setRefreshToken(response.refreshToken)
        initUUID()
        Single.just(response)
    }.flatMapCompletable {
        val jwt = it.accessToken.substring("Bearer ".length)
        decodeAndSave(jwt)
    }

    private fun initUUID() {
        val uuid = UUID.randomUUID().toString()
        preferences.setUUID(uuid)
    }

    @Throws(Exception::class)
    private fun decodeAndSave(jwtEncoded: String): Completable {
        try {
            val split =
                jwtEncoded.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val dataAboutUser = getJson(split[1])
            val parseDate = JsonParser.parseString(dataAboutUser)
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
        profileId: Long
    ) = Completable.fromAction {
        preferences.setUserName(userName)
        preferences.setUserAvatar(avatarUrl)
        preferences.setEmail(email)
        preferences.setProfileId(profileId)
    }

    private fun getJson(strEncoded: String): String {
        val decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes, charset("UTF-8"))
    }

    override fun requestResetPasswordCode(email: String): Completable =
        loginWebservice.requestResetPasswordCode(email)

    override fun sendResetPasswordCode(email: String, code: String): Completable =
        loginWebservice.sendResetPasswordCode(email, code)

    override fun resetPassword(email: String, code: String, password: String): Completable =
        loginWebservice.resetPassword(email, code, password).saveAuthData()

}