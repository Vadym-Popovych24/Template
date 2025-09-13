package com.android.template.ui.base

import com.android.template.R
import com.android.template.data.models.api.ErrorLiveData
import com.android.template.data.models.api.ResultLiveData
import com.android.template.data.models.api.SuccessLiveData
import com.android.template.data.models.exception.ApproveException
import com.android.template.data.models.exception.SignInException
import com.android.template.data.models.exception.UserAlreadyExistException
import com.android.template.data.models.exception.UserNotFoundException
import com.android.template.testutils.ViewModelTest
import com.android.template.utils.helpers.ResourceProvider
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.FORBIDDEN
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.INTERNAL_SERVER_ERROR
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.INVALID_USERNAME_OR_PASSWORD
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.NOT_FOUND
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.UNKNOWN
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest : ViewModelTest() {

    private lateinit var viewModel: TestViewModel
    val callback = mockk<(String?) -> Unit>(relaxed = true)
    val mockResourceProvider = mockk<ResourceProvider>()
    val baseViewModel = object : BaseViewModel() {
        override val resourceProvider = mockResourceProvider
    }

    @Before
    fun setupBaseViewModel() {
        viewModel = TestViewModel(testDispatcher, mockResourceProvider)
        baseViewModel.messageCallback = callback
        // Make Rx run synchronously for testing
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun teardown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun intoPostsSuccessResultAndUpdatesIsLoading() = runTest {
        val liveResult = MutableLiveResult<ResultLiveData<String>>()

        viewModel.into(liveResult) {
            delay(100)
            SuccessLiveData("OK")
        }

        // Loading should be true immediately
        assertTrue(viewModel.isLoading.get())

        // Advance time for delay
        advanceUntilIdle()

        // Now loading should be false
        assertTrue(!viewModel.isLoading.get())

        // Assert success was posted
        val result = liveResult.value
        assertTrue(result is SuccessLiveData<*>)
        assertEquals(SuccessLiveData("OK"), (result as SuccessLiveData<*>).value)
    }

    @Test
    fun intoPostsErrorOnFailure() = runTest {
        val liveResult = MutableLiveResult<ResultLiveData<String>>()

        viewModel.into(liveResult) {
            throw RuntimeException("Boom")
        }

        advanceUntilIdle()

        val result = liveResult.value
        assertTrue(result is ErrorLiveData)
        assertEquals("Boom", (result as ErrorLiveData).error.message)
    }

    @Test
    fun intoIgnoresCancellationException() = runTest {
        val liveResult = MutableLiveResult<ResultLiveData<String>>()

        viewModel.into(liveResult) {
            throw CancellationException("Cancelled")
        }

        advanceUntilIdle()

        // Should not post anything on CancellationException
        assertEquals(null, liveResult.value)
    }

    @Test
    fun makeRxSingleShouldUpdateLoadingInvokeCallbackAndResetLoading() {
        // Arrange
        val expectedResult = "Success!"
        val single = Single.just(expectedResult)
        val callback: (String) -> Unit = mockk(relaxed = true)
        val loadingCallback: (Boolean) -> Unit = mockk(relaxed = true)

        viewModel.loadingCallback = loadingCallback

        // Act
        viewModel.makeRx(single, callback)

        // Assert using MockK's verify
        verify { callback(expectedResult) }
        verifySequence {
            loadingCallback(true)
            loadingCallback(false)
        }
        assertFalse(viewModel.isLoading.get())
        assertEquals(1, viewModel.compositeDisposable.size())
    }

    @Test
    fun makeRxSingleShouldHandleError() {
        val error = RuntimeException("Something went wrong")
        val single = Single.error<String>(error)

        val callback: (String) -> Unit = mockk(relaxed = true)
        val loadingCallback: (Boolean) -> Unit = mockk(relaxed = true)

        // Spy on your class to verify handleError

        viewModel.loadingCallback = loadingCallback

        // Act
        viewModel.makeRx(single, callback)

        // Assert

        verify { callback wasNot called }
        verifySequence {
            loadingCallback(true)
            loadingCallback(false)
        }
        assertFalse(viewModel.isLoading.get())
        assertEquals(error, viewModel.lastError)
    }

    @Test
    fun makeRxSingleShouldWorkWithoutLoadingCallback() {
        // Arrange
        val expectedResult = "Success!"
        val single = Single.just(expectedResult)
        val callback: (String) -> Unit = mockk(relaxed = true)

        // Note: loadingCallback is NOT set — remains null

        // Act
        viewModel.makeRx(single, callback)

        // Assert
        verify { callback(expectedResult) }
        assertFalse(viewModel.isLoading.get())
    }


    @Test
    fun makeRxCompletableShouldUpdateLoadingInvokeCallbackAndResetLoading() {
        // Arrange
        val completable = Completable.complete()
        val callback: () -> Unit = mockk(relaxed = true)
        val loadingCallback: (Boolean) -> Unit = mockk(relaxed = true)

        viewModel.loadingCallback = loadingCallback

        // Act
        viewModel.makeRx(completable, callback)

        // Assert using MockK's verify
        verify { callback() }
        verifySequence {
            loadingCallback(true)
            loadingCallback(false)
        }
        assertFalse(viewModel.isLoading.get())
        assertEquals(1, viewModel.compositeDisposable.size())
    }

    @Test
    fun makeRxCompletableShouldHandleError() {
        val error = RuntimeException("Something went wrong")
        val completable = Completable.error(error)
        val callback: () -> Unit = mockk(relaxed = true)
        val loadingCallback: (Boolean) -> Unit = mockk(relaxed = true)

        // Spy on your class to verify handleError

        viewModel.loadingCallback = loadingCallback

        // Act
        viewModel.makeRx(completable, callback)

        // Assert

        verify { callback wasNot called }
        verifySequence {
            loadingCallback(true)
            loadingCallback(false)
        }
        assertFalse(viewModel.isLoading.get())
        assertEquals(error, viewModel.lastError)
    }

    @Test
    fun makeRxCompletableShouldWorkWithoutLoadingAndCompleteCallback() {
        // Arrange
        val completable = Completable.complete()

        // Note: val callback: () -> Unit = mockk(relaxed = true)  is NOT set — remains null

        // Note: loadingCallback is NOT set — remains null

        // Act
        viewModel.makeRx(completable)

        // Assert
        assertFalse(viewModel.isLoading.get())
    }


    @Test
    fun makeRxInvisibleSingleShouldUpdateLoadingInvokeCallbackAndResetLoading() {
        // Arrange
        val expectedResult = "Success!"
        val single = Single.just(expectedResult)
        val callback: (String) -> Unit = mockk(relaxed = true)
        val loadingCallback: (Boolean) -> Unit = mockk(relaxed = true)

        viewModel.loadingCallback = loadingCallback

        // Act
        viewModel.makeRxInvisible(single, callback)

        // Assert using MockK's verify
        verify { loadingCallback wasNot called }
        assertFalse(viewModel.isLoading.get())
        verify { callback(expectedResult) }
        assertEquals(1, viewModel.compositeDisposable.size())
    }

    @Test
    fun makeRxInvisibleSingleShouldNotCrashOnError() {
        val errorSingle = Single.error<String>(RuntimeException("Test error"))
        val callback: (String) -> Unit = mockk(relaxed = true)

        viewModel.makeRxInvisible(errorSingle, callback)

        // No callback should be called
        verify(exactly = 0) { callback(any()) }
        assertEquals(1, viewModel.compositeDisposable.size())
    }


    @Test
    fun makeRxInvisibleCompletableShouldUpdateLoadingInvokeCallbackAndResetLoading() {
        // Arrange
        val completable = Completable.complete()
        val callback: () -> Unit = mockk(relaxed = true)
        val loadingCallback: (Boolean) -> Unit = mockk(relaxed = true)

        viewModel.loadingCallback = loadingCallback

        // Act
        viewModel.makeRxInvisible(completable, callback)

        // Assert using MockK's verify
        verify { loadingCallback wasNot called }
        assertFalse(viewModel.isLoading.get())
        verify { callback() }
        assertEquals(1, viewModel.compositeDisposable.size())
    }

    @Test
    fun makeRxInvisibleCompletableShouldNotCrashOnError() {
        val errorCompletable = Completable.error(RuntimeException("Test error"))
        val callback: () -> Unit = mockk(relaxed = true)

        viewModel.makeRxInvisible(errorCompletable, callback)

        // No callback should be called
        verify(exactly = 0) { callback() }
        assertEquals(1, viewModel.compositeDisposable.size())
    }

    @Test
    fun makeRxInvisibleCompletableShouldWorkWithoutCompleteCallback() {
        val completable = Completable.complete()

        viewModel.makeRxInvisible(completable)

        assertEquals(1, viewModel.compositeDisposable.size())
    }

    @Test
    fun showMessageShouldInvokeMessageCallback() {
        // Arrange
        val expectedMessage = "Test message"
        val callback: (String?) -> Unit = mockk(relaxed = true)

        viewModel.messageCallback = callback

        // Act
        viewModel.showMessage(expectedMessage)

        // Assert
        verify { callback(expectedMessage) }
    }

    @Test
    fun showMessageShouldNotCrashWhenCallbackIsNull() {
        // messageCallback is left null

        // Act
        viewModel.showMessage("This message should not crash")
    }

    @Test
    fun showMessageIntShouldInvokeMessageCallbackWithResolvedString() {
        // Arrange

        val callback = mockk<(String?) -> Unit>(relaxed = true)
        viewModel.messageCallback = callback

        val resId = R.string.app_name
        val expectedMessage = "Mocked String"
        every { mockResourceProvider.getString(resId) } returns expectedMessage

        // Act
        viewModel.showMessage(resId)

        // Assert
        verify { callback.invoke(expectedMessage) }
    }

    @Test
    fun showMessageIntShouldNotCrashWhenCallbackIsNull() {
        // messageCallback is left null

        // Act
        viewModel.showMessage(R.string.app_name)
    }

    @Test
    fun onClearedShouldDisposeCompositeDisposableAndCancelCoroutineScope() {
        // Arrange
        assertTrue(!viewModel.compositeDisposable.isDisposed)
        assertFalse(viewModel.isJobCancelled())

        // Act
        viewModel.callOnCleared()

        // Assert
        assertTrue(viewModel.compositeDisposable.isDisposed)
        assertTrue(viewModel.isScopeCancelled())
    }

    @Test
    fun handleErrorShouldShowNoInternetMessageWhenThrowableContainsKnownErrorMessage() {
        // Arrange
        val errorMessage = "No address associated"
        val expectedMessage = "No internet connection"

        every { mockResourceProvider.getString(R.string.no_address_associated) } returns errorMessage
        every { mockResourceProvider.getString(R.string.no_internet_error) } returns expectedMessage

        // Act
        baseViewModel.handleError(Throwable("No address associated with hostname"))

        // Assert
        verify { callback.invoke(expectedMessage) }
    }

    @Test
    fun handleErrorShouldShowMessageForSignInException() {
        every { mockResourceProvider.getString(R.string.invalid_username_or_password) } returns "Invalid login"
        baseViewModel.handleError(SignInException())
        verify { callback.invoke("Invalid login") }
    }

    @Test
    fun handleErrorShouldShowMessageForUserNotFoundException() {
        every { mockResourceProvider.getString(R.string.no_user_with_email_error) } returns "No user"
        baseViewModel.handleError(UserNotFoundException())
        verify { callback("No user") }
    }

    @Test
    fun handleErrorShouldShowMessageForApproveException() {
        every { mockResourceProvider.getString(R.string.request_token_not_approved) } returns "Not approved"
        baseViewModel.handleError(ApproveException())
        verify { callback("Not approved") }
    }

    @Test
    fun handleErrorShouldShowMessageForUserAlreadyExistException() {
        every { mockResourceProvider.getString(R.string.sign_up_duplicate_error_simple) } returns "User exists"
        baseViewModel.handleError(UserAlreadyExistException())
        verify { callback("User exists") }
    }

    @Test
    fun handleErrorShouldShowForbiddenErrorFromHttpException() {
        val exception = mockk<HttpException>()
        every { exception.code() } returns FORBIDDEN
        every { exception.message() } returns ""
        every { mockResourceProvider.getString(R.string.forbidden_error) } returns "Forbidden"
        baseViewModel.handleError(exception)
        verify { callback("Forbidden") }
    }

    @Test
    fun handleErrorShouldShowNotFoundErrorFromHttpException() {
        val exception = mockk<HttpException>()
        every { exception.code() } returns NOT_FOUND
        every { exception.message() } returns ""
        every { mockResourceProvider.getString(R.string.not_found) } returns "Not found"
        baseViewModel.handleError(exception)
        verify { callback("Not found") }
    }

    @Test
    fun handleErrorShouldShowInvalidUsernameOrPasswordErrorFromHttpException() {
        val exception = mockk<HttpException>()
        every { exception.code() } returns INVALID_USERNAME_OR_PASSWORD
        every { exception.message() } returns ""
        every { mockResourceProvider.getString(R.string.invalid_username_or_password) } returns "Invalid username or password"
        baseViewModel.handleError(exception)
        verify { callback("Invalid username or password") }
    }

    @Test
    fun handleErrorShouldShowInternalServerErrorFromHttpException() {
        val exception = mockk<HttpException>()
        every { exception.code() } returns INTERNAL_SERVER_ERROR
        every { exception.message() } returns ""
        every { mockResourceProvider.getString(R.string.internal_server_error) } returns "Internal server error"
        baseViewModel.handleError(exception)
        verify { callback("Internal server error") }
    }

    @Test
    fun handleErrorShouldShowRawMessageWhenJSONParsingFails() {
        val exception = mockk<HttpException>()
        every { exception.code() } returns UNKNOWN
        every { exception.message() } returns "Non-JSON message"
        baseViewModel.handleError(exception)
        verify { callback("Non-JSON message") }
    }

    @Test
    fun handleErrorShouldShowHTTPCodeIfNoneMatchedAndNotZero() {
        val exceptionCode = 402
        val exception = mockk<HttpException>(relaxed = true) {
            every { code() } returns exceptionCode
            every { message() } returns ""
        }
        baseViewModel.handleError(exception)
        verify { callback(exceptionCode.toString()) }
    }

    @Test
    fun handleErrorShouldNotShowHTTPCodeIfMatchedCodeIsZero() {
        val exception = mockk<HttpException>(relaxed = true)
        val exceptionCode = 0
        every { exception.code() } returns exceptionCode
        every { exception.message() } returns ""
        baseViewModel.handleError(exception)
        verify { callback wasNot called }
    }

    @Test
    fun handleErrorShouldCallCallbackWithRawMessageForUnknownException() {
        every { mockResourceProvider.getString(any()) } returns "dummy"
        val exception = RuntimeException("Unknown error")
        baseViewModel.handleError(exception)
        verify { callback("Unknown error") }
    }

    // Fake ViewModel for testing
        class TestViewModel(
            dispatcher: CoroutineDispatcher,
            override val resourceProvider: ResourceProvider
        ) : BaseViewModel() {
            init {
                // Replace default coroutineScope with test dispatcher (optional)
                val jobField = BaseViewModel::class.java.getDeclaredField("coroutineScope")
                jobField.isAccessible = true
                jobField.set(this, CoroutineScope(dispatcher + SupervisorJob()))
            }

            var lastError: Throwable? = null

            override fun handleError(error: Throwable) {
                lastError = error
            }

            fun callOnCleared() {
                onCleared()
            }


            fun isScopeCancelled(): Boolean {
                val coroutineScopeField = BaseViewModel::class.java.getDeclaredField("coroutineScope")
                coroutineScopeField.isAccessible = true
                val coroutineScope = coroutineScopeField.get(this) as CoroutineScope
                return!coroutineScope.isActive
            }

            fun isJobCancelled(): Boolean {
                val jobField = BaseViewModel::class.java.getDeclaredField("job")
                jobField.isAccessible = true
                val job = jobField.get(this) as Job
                return job.isCancelled
            }
        }
}