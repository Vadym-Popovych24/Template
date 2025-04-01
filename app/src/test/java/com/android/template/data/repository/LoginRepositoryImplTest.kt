package com.android.template.data.repository

import com.android.template.data.local.interfaces.ProfilesStorage
import com.android.template.data.models.api.model.AccountWithSession
import com.android.template.data.models.db.ProfileEntity
import com.android.template.data.models.exception.ApproveException
import com.android.template.data.models.exception.SignInException
import com.android.template.data.models.exception.UserAlreadyExistException
import com.android.template.data.models.exception.UserNotFoundException
import com.android.template.data.prefs.PreferencesHelper
import com.android.template.data.remote.interfaces.LoginWebservice
import com.android.template.data.remote.interfaces.ProfileWebservice
import com.android.template.data.repository.impl.LoginRepositoryImpl
import com.android.template.testutils.createAccountResponse
import com.android.template.testutils.createProfileEntity
import com.android.template.testutils.createRequestKeyResponse
import com.android.template.testutils.createSessionResponse
import com.android.template.testutils.createSignUpProfileData
import io.mockk.Runs
import io.mockk.called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test

class LoginRepositoryImplTest {

    private lateinit var loginRepository: LoginRepositoryImpl
    private val profilesStorage: ProfilesStorage = mockk()
    private val loginWebservice: LoginWebservice = mockk()
    private val profileWebservice: ProfileWebservice = mockk()
    private val preferences: PreferencesHelper = mockk(relaxed = true)

    @Before
    fun setUp() {
        loginRepository = LoginRepositoryImpl(
            loginWebservice = loginWebservice,
            profileWebservice = profileWebservice,
            preferences = preferences,
            profilesStorage = profilesStorage
        )
    }

    @Test
    fun requestTokenShouldReturnErrorIfEmailAlreadyExists() {
        val email = "test@example.com"
        val profileEntity = createProfileEntity(email)

        every { profilesStorage.getProfileByEmailIgnoreEmpty(email) } returns Single.just(
            profileEntity
        )

        val testObserver = loginRepository.requestToken(email).test()

        testObserver.assertError(UserAlreadyExistException::class.java)
        verify(exactly = 1) { profilesStorage.getProfileByEmailIgnoreEmpty(email) }
        verify { loginRepository.requestToken(email = email) wasNot called }
        confirmVerified(profilesStorage, loginWebservice)
    }

    @Test
    fun requestTokenShouldReturnNewRequestTokenIfEmailDoesNotExist() {
        val email = "newuser@example.com"
        val requestKeyResponse = createRequestKeyResponse()

        every { profilesStorage.getProfileByEmailIgnoreEmpty(email) } returns Single.just(
            ProfileEntity.getEmptyProfileEntity()
        )
        every { loginWebservice.requestToken() } returns Single.just(requestKeyResponse)

        val testObserver = loginRepository.requestToken(email).test()

        testObserver.assertValue { response ->
            response.success && response.requestToken == "new_request_token"
        }

        verify(exactly = 1) { profilesStorage.getProfileByEmailIgnoreEmpty(email) }
        verify(exactly = 1) { loginWebservice.requestToken() }
        confirmVerified(profilesStorage, loginWebservice)
    }

    @Test
    fun authenticateAccountShouldCompleteSuccessfullyWhenAPICallsSucceed() {
        val requestToken = "test_token"
        val sessionId = "session_123"
        val sessionResponse = createSessionResponse(sessionId)
        val signUpProfileData = createSignUpProfileData()
        val accountResponse = createAccountResponse()
        val accountResponseWithSession = AccountWithSession(accountResponse, sessionId)

        every { loginWebservice.approveRequestToken(requestToken) } returns Completable.complete()
        every { loginWebservice.createSession(requestToken) } returns Single.just(sessionResponse)
        every { profileWebservice.getAccount(sessionId) } returns Single.just(
            accountResponseWithSession
        )
        every { profilesStorage.getProfileIdByEmail(signUpProfileData.email) } returns 1L
        every { profilesStorage.insertProfile(any()) } just Runs

        val testObserver = loginRepository.authenticateAccount(requestToken, signUpProfileData)
            .test()

        testObserver.assertComplete()
        verify(exactly = 1) { loginWebservice.approveRequestToken(requestToken) }
        verify(exactly = 1) { loginWebservice.createSession(requestToken) }
        verify(exactly = 1) { profileWebservice.getAccount(sessionId) }
        verify(exactly = 1) { profilesStorage.insertProfile(any()) }
        verify(exactly = 1) { profilesStorage.getProfileIdByEmail(signUpProfileData.email) }

        // Verify `savePreferences` is indirectly called via its effects
        verify(exactly = 1) { preferences.setRequestToken(requestToken) }
        verify(exactly = 1) { preferences.setEmail(signUpProfileData.email) }
        verify(exactly = 1) { preferences.setUserName("${signUpProfileData.firstName} ${signUpProfileData.lastName}") }
        verify(exactly = 1) { preferences.setUserAvatar(accountResponseWithSession.accountResponse.avatar.tmdb.avatarPath) }
        verify(exactly = 1) { preferences.setDBProfileId(1L) }
    }

    @Test
    fun authenticateAccountShouldFailWhenApproveRequestTokenFails() {
        val requestToken = "test_token"
        val sessionId = "session_123"
        val sessionResponse = createSessionResponse(sessionId)
        val signUpProfileData = createSignUpProfileData()

        every { loginWebservice.approveRequestToken(requestToken) } returns Completable.error(
            ApproveException()
        )
        every { loginWebservice.createSession(requestToken) } returns Single.just(sessionResponse)

        val testObserver = loginRepository.authenticateAccount(requestToken, signUpProfileData)
            .test()

        testObserver.assertNotComplete()
        testObserver.assertError(Exception::class.java)
        verify { loginWebservice.approveRequestToken(requestToken) }
        verify { loginWebservice.createSession(any()) wasNot called }
        verify(exactly = 0) { profileWebservice.getAccount(any()) }
        verify(exactly = 0) { profilesStorage.insertProfile(any()) }
    }

    @Test
    fun authByDBSuccess() {
        val email = "test@example.com"
        val password = "password123"
        val profileEntity = createProfileEntity(
            email = email,
            password = password
        )
        val profileId = 1L

        every { profilesStorage.getProfileByEmail(email) } returns Single.just(profileEntity)
        every { profilesStorage.getProfileIdByEmail(email) } returns profileId
        every { preferences.setRequestToken(profileEntity.requestToken) } just Runs
        every { preferences.setEmail(profileEntity.email) } just Runs
        every { preferences.setUserName(profileEntity.userName) } just Runs
        every { preferences.setUserAvatar(profileEntity.avatarPath) } just Runs
        every { preferences.setDBProfileId(profileId) } just Runs

        loginRepository.authByDB(email, password)
            .test()
            .assertComplete()

        verify { preferences.setRequestToken(profileEntity.requestToken) }
        verify { preferences.setEmail(profileEntity.email) }
        verify { preferences.setUserName("${profileEntity.firstName} ${profileEntity.lastName}") }
        verify { preferences.setUserAvatar(profileEntity.avatarPath) }
        verify { preferences.setDBProfileId(1) }
    }

    @Test
    fun authByDBFailsWithIncorrectPassword() {
        val email = "test@example.com"
        val password = "wrong_password"
        val profileEntity = createProfileEntity(
            email = email,
            password = "password123"
        )

        every { profilesStorage.getProfileByEmail(email) } returns Single.just(profileEntity)

        loginRepository.authByDB(email, password)
            .test()
            .assertError(SignInException::class.java)
    }

    @Test
    fun authByDBFailsWhenProfileNotFound() {
        val email = "unknown@example.com"
        val password = "password123"

        every { profilesStorage.getProfileByEmail(email) } returns Single.error(
            UserNotFoundException()
        )

        loginRepository.authByDB(email, password)
            .test()
            .assertError(UserNotFoundException::class.java)
    }

}