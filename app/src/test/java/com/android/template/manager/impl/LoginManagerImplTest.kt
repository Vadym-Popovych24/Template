package com.android.template.manager.impl

import com.android.template.data.repository.interfaces.LoginRepository
import com.android.template.testutils.createRequestKeyResponse
import com.android.template.testutils.createSignUpProfileData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test


class LoginManagerImplTest {

    private val loginRepository: LoginRepository = mockk()
    private lateinit var loginManager: LoginManagerImpl

    @Before
    fun setup() {
        loginManager = LoginManagerImpl(loginRepository)
    }

    @Test
    fun requestTokenDelegatesToRepository() {
        val email = "test@example.com"
        val expectedResponse = createRequestKeyResponse()
        every { loginRepository.requestToken(email) } returns Single.just(expectedResponse)

        loginManager.requestToken(email)
            .test()
            .assertNoErrors()
            .assertValue(expectedResponse)

        verify(exactly = 1) { loginRepository.requestToken(email) }
    }

    @Test
    fun authenticateAccountDelegatesToRepository() {
        val token = "token123"
        val profileData = createSignUpProfileData()
        every {
            loginRepository.authenticateAccount(
                token,
                profileData
            )
        } returns Completable.complete()

        loginManager.authenticateAccount(token, profileData)
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { loginRepository.authenticateAccount(token, profileData) }
    }

    @Test
    fun authByDBDelegatesToRepository() {
        val email = "email@example.com"
        val password = "password"
        every { loginRepository.authByDB(email, password) } returns Completable.complete()

        loginManager.authByDB(email, password)
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { loginRepository.authByDB(email, password) }
    }

    @Test
    fun authDelegatesToRepository() {
        val email = "email@example.com"
        val password = "password"
        every { loginRepository.auth(email, password) } returns Completable.complete()

        loginManager.auth(email, password)
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { loginRepository.auth(email, password) }
    }

    @Test
    fun signUpDelegatesToRepository() {
        val firstName = "Name"
        val lastName = "LastName"
        val email = "test@example.com"
        val password = "password123"
        every {
            loginRepository.signUp(
                firstName,
                lastName,
                email,
                password
            )
        } returns Completable.complete()

        loginManager.signUp(firstName, lastName, email, password)
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { loginRepository.signUp(firstName, lastName, email, password) }
    }

    @Test
    fun requestResetPasswordCodeDelegatesToRepository() {
        val email = "email@example.com"
        every { loginRepository.requestResetPasswordCode(email) } returns Completable.complete()

        loginManager.requestResetPasswordCode(email)
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { loginRepository.requestResetPasswordCode(email) }
    }

    @Test
    fun sendResetPasswordCodeDelegatesToRepository() {
        val email = "email@example.com"
        val code = "123456"
        every { loginRepository.sendResetPasswordCode(email, code) } returns Completable.complete()

        loginManager.sendResetPasswordCode(email, code)
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { loginRepository.sendResetPasswordCode(email, code) }
    }

    @Test
    fun resetPasswordDelegatesToRepository() {
        val email = "email@example.com"
        val code = "123456"
        val password = "newPass"
        every {
            loginRepository.resetPassword(
                email,
                code,
                password
            )
        } returns Completable.complete()

        loginManager.resetPassword(email, code, password)
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { loginRepository.resetPassword(email, code, password) }
    }

    @Test
    fun saveFCMTokenDelegatesToRepository() {
        val token = "fcm_token_123"
        every { loginRepository.saveFCMToken(token) } returns Completable.complete()

        loginManager.saveFCMToken(token)
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { loginRepository.saveFCMToken(token) }
    }
}
