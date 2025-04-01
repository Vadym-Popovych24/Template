package com.android.template.data.remote.impl

import android.content.Context
import com.android.template.data.models.api.request.SessionRequest
import com.android.template.data.models.api.response.LoginResponse
import com.android.template.data.models.api.response.RequestKeyResponse
import com.android.template.data.models.api.response.SessionResponse
import com.android.template.data.models.exception.ApproveException
import com.android.template.data.remote.api.ApiEndpoints
import com.android.template.data.remote.api.LoginApi
import com.android.template.data.remote.api.WebApi
import com.android.template.R
import com.android.template.TemplateApp
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit

@RunWith(JUnit4::class)
class LoginWebserviceImplTest {

    private lateinit var loginWebservice: LoginWebserviceImpl
    private val mockBaseRetrofit: Retrofit = mockk()
    private val mockWebRetrofit: Retrofit = mockk()
    private val mockLoginApi: LoginApi = mockk()
    private val mockWebApi: WebApi = mockk()
    private val mockContext: Context = mockk()

    @Before
    fun setup() {
        mockkObject(TemplateApp)
        every { TemplateApp.appContext } returns mockContext

        every { TemplateApp.appContext.applicationContext } returns mockContext

        // Mock Retrofit.create() method
        every { mockBaseRetrofit.create(LoginApi::class.java) } returns mockLoginApi
        every { mockWebRetrofit.create(WebApi::class.java) } returns mockWebApi

        // Initialize the service with mocked Retrofit
        loginWebservice = LoginWebserviceImpl(mockBaseRetrofit, mockWebRetrofit)
    }

    @Test
    fun testLoginApiIsCorrectlyInitialized() {
        assertNotNull(loginWebservice.loginApi)
        verify { mockBaseRetrofit.create(LoginApi::class.java) } // Verify that create() was called
    }

    @Test
    fun testWebApiISCorrectlyInitialized() {
        assertNotNull(loginWebservice.webApi)
        verify { mockWebRetrofit.create(WebApi::class.java) } // Verify that create() was called
    }

    @Test
    fun testLoginApiIsInstanceOfLoginApi() {
        assertTrue(loginWebservice.loginApi is LoginApi)
        verify { mockBaseRetrofit.create(LoginApi::class.java) }
    }

    @Test
    fun testWebApiIsInstanceOfWebApi() {
        assertTrue(loginWebservice.webApi is WebApi)
        verify { mockWebRetrofit.create(WebApi::class.java) }
    }

    @Test
    fun testRequestToken() {
        // Given
        val expectedResponse = RequestKeyResponse(success = true, expiresAt = "600", requestToken = "valid_token")

        every {
            mockLoginApi.requestToken(ApiEndpoints.API_KEY)
        } returns Single.just(expectedResponse)

        // When
        val testObserver = loginWebservice.requestToken()
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(expectedResponse)

        // Verify API was called exactly once
        verify(exactly = 1) { mockLoginApi.requestToken(ApiEndpoints.API_KEY) }
    }

    @Test
    fun approveRequestTokenCompletesSuccessfully() {
        // Given
        val requestToken = "valid_token"
        every { mockWebApi.approveRequestToken(requestToken) } returns Completable.complete()

        // When
        val testObserver = loginWebservice.approveRequestToken(requestToken)
            .subscribeOn(Schedulers.trampoline()) // Ensure it runs on the same thread
            .test()

        // Then
        testObserver.assertComplete()
        testObserver.assertNoErrors()

        // Verify that the API was called
        verify(exactly = 1) { mockWebApi.approveRequestToken(requestToken) }
    }

    @Test
    fun approveRequestTokenThrowsApproveExceptionOn401Error() {
        // Given
        val requestToken = "invalid_token"
        val responseBody = "Unauthorized".toResponseBody("application/json".toMediaTypeOrNull())

        val httpException = HttpException(Response.error<Any>(401, responseBody))

        every { mockWebApi.approveRequestToken(requestToken) } returns Completable.error(httpException)

        // When & Then
        val testObserver = loginWebservice.approveRequestToken(requestToken)
            .subscribeOn(Schedulers.trampoline())
            .test()

        testObserver.assertError(ApproveException::class.java)
        testObserver.assertNotComplete()

        // Ensure that the API was called once
        verify(exactly = 1) { mockWebApi.approveRequestToken(requestToken) }
    }

    @Test
    fun approveRequestTokenPropagatesHttpExceptionForOtherErrorCodes() {
        // Given
        val requestToken = "some_token"
        val responseBody = "Forbidden".toResponseBody("application/json".toMediaTypeOrNull())

        val httpException = HttpException(Response.error<Any>(403, responseBody)) // 403, not 401

        every { mockWebApi.approveRequestToken(requestToken) } returns Completable.error(httpException)

        // When & Then
        val testObserver = loginWebservice.approveRequestToken(requestToken)
            .subscribeOn(Schedulers.trampoline())
            .test()

        testObserver.assertError(HttpException::class.java) // Should not be ApproveException
        testObserver.assertNotComplete()

        // Ensure that the API was called once
        verify(exactly = 1) { mockWebApi.approveRequestToken(requestToken) }
    }

    @Test
    fun approveRequestTokenPropagatesNonHTTPErrors() {
        // Given
        val requestToken = "random_token"
        val networkException = RuntimeException("Network error")

        every { mockWebApi.approveRequestToken(requestToken) } returns Completable.error(networkException)

        // When & Then
        val testObserver = loginWebservice.approveRequestToken(requestToken)
            .subscribeOn(Schedulers.trampoline())
            .test()

        testObserver.assertError(RuntimeException::class.java)
        testObserver.assertNotComplete()

        // Ensure that the API was called once
        verify(exactly = 1) { mockWebApi.approveRequestToken(requestToken) }
    }

    @Test
    fun createSessionReturnsSessionResponseOnSuccess() {
        // Given
        val requestToken = "valid_token"
        val expectedResponse = SessionResponse(success = true, sessionId = "session_123")

        every {
            mockLoginApi.createSession(ApiEndpoints.API_KEY, SessionRequest(requestToken))
        } returns Single.just(expectedResponse)

        // When
        val testObserver = loginWebservice.createSession(requestToken)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(expectedResponse)

        // Verify API was called exactly once
        verify(exactly = 1) { mockLoginApi.createSession(ApiEndpoints.API_KEY, SessionRequest(requestToken)) }
    }

    @Test
    fun loginReturnsLoginResponseOnSuccess() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val expectedResponse = LoginResponse(accessToken = "access_token", expiresIn = 600, refreshToken = "refresh_token")

        every {
            mockLoginApi.login(email, password)
        } returns Single.just(expectedResponse)

        // When
        val testObserver = loginWebservice.login(email, password)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(expectedResponse)

        // Verify API was called exactly once
        verify(exactly = 1) { mockLoginApi.login(email, password) }
    }

    @Test
    fun signUpReturnsLoginResponseOnsuccess() {
        // Given
        val firstName = "name"
        val lastName = "lastName"
        val email = "email@example.com"
        val password = "secret"

        val expectedResponse = LoginResponse(accessToken = "access_token", expiresIn = 600, refreshToken = "refresh_token")

        every {
            mockLoginApi.signUp(firstName, lastName, email, password)
        } returns Single.just(expectedResponse)

        // When
        val testObserver = loginWebservice.signUp(firstName, lastName, email, password)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(expectedResponse)

        // Verify API was called exactly once
        verify(exactly = 1) { mockLoginApi.signUp(firstName, lastName, email, password) }
    }

    @Test
    fun signUpReturnsHttpExceptionOn400Error() {
        // Given
        val firstName = "name"
        val lastName = "lastName"
        val email = "email@example.com"
        val password = "weak"

        val responseBody = """{ "error": "Invalid password" }"""
            .toResponseBody("application/json".toMediaTypeOrNull())

        val httpException = HttpException(Response.error<Any>(400, responseBody))

        every {
            mockLoginApi.signUp(firstName, lastName, email, password)
        } returns Single.error(httpException)

        // When
        val testObserver = loginWebservice.signUp(firstName, lastName, email, password)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertError(HttpException::class.java)
        testObserver.assertNotComplete()

        // Verify API was called exactly once
        verify(exactly = 1) { mockLoginApi.signUp(firstName, lastName, email, password) }
    }

    @Test
    fun signUpPropagatesNetworkError() {
        // Given
        val firstName = "John"
        val lastName = "Doe"
        val email = "john.doe@example.com"
        val password = "secret"

        val networkException = RuntimeException("Network error")

        every {
            mockLoginApi.signUp(firstName, lastName, email, password)
        } returns Single.error(networkException)

        // When
        val testObserver = loginWebservice.signUp(firstName, lastName, email, password)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertError(RuntimeException::class.java)
        testObserver.assertNotComplete()

        // Verify API was called exactly once
        verify(exactly = 1) { mockLoginApi.signUp(firstName, lastName, email, password) }
    }

    @Test
    fun requestResetPasswordCodeCompletesSuccessfully() {
        // Given
        val email = "test@example.com"

        // When
        val testObserver = loginWebservice.requestResetPasswordCode(email)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun sendResetPasswordCodeCompleteSuccessfullyWhenCodeIsCorrect() {
        // Given
        val email = "test@example.com"
        val correctCode = "123456"

        // When
        val testObserver = loginWebservice.sendResetPasswordCode(email, correctCode)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun sendResetPasswordCodeReturnsErrorWhenCodeIsIncorrect() {
        // Given
        val email = "test@example.com"
        val wrongCode = "654321"

        // Mock getString() to return a fake error message
        every { mockContext.getString(R.string.wrong_secure_code_error) } returns "Wrong secure code!"

        // When
        val testObserver = loginWebservice.sendResetPasswordCode(email, wrongCode)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertError(IllegalArgumentException::class.java)
        testObserver.assertNotComplete()
    }

    @Test
    fun resetPasswordCompletesSuccessfullyWhenNoErrorOccurs() {
        // Given
        val email = "test@example.com"
        val code = "123456"
        val password = "newPassword123"

        // When
        val testObserver: TestObserver<LoginResponse> = loginWebservice.resetPassword(email, code, password)
            .subscribeOn(Schedulers.trampoline()) // Ensures immediate execution
            .test()

        // Then
        testObserver.assertComplete()
        testObserver.assertNoErrors()

        // Verify the emitted response
        val response = testObserver.values().first()
        assertEquals("accessToken", response.accessToken)
        assertEquals(600, response.expiresIn)
        assertEquals("refreshToken", response.refreshToken)
    }

    @Test
    fun resetPasswordReturnsErrorWhenExceptionOccurs() {
        // Given
        val email = "test@example.com"
        val code = "wrong_code"
        val password = "newPassword123"

        every { mockContext.getString(R.string.wrong_password_error) } returns "Wrong password"

        // When
        val testObserver: TestObserver<LoginResponse> = loginWebservice.resetPassword(email, code, password)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertError(IllegalArgumentException::class.java)
    }
}