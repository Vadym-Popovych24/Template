package com.android.template.ui.base

import com.android.template.data.models.api.ErrorLiveData
import com.android.template.data.models.api.ResultLiveData
import com.android.template.data.models.api.SuccessLiveData
import com.android.template.testutils.ViewModelTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest : ViewModelTest() {

    private lateinit var viewModel: TestViewModel

    @Before
    fun setupBaseViewModel() {
        viewModel = TestViewModel(testDispatcher)
    }

    @Test
    fun `into posts Success result and updates isLoading`() = runTest {
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
    fun `into posts Error on failure`() = runTest {
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
    fun `into ignores CancellationException`() = runTest {
        val liveResult = MutableLiveResult<ResultLiveData<String>>()

        viewModel.into(liveResult) {
            throw CancellationException("Cancelled")
        }

        advanceUntilIdle()

        // Should not post anything on CancellationException
        assertEquals(null, liveResult.value)
    }

    // Fake ViewModel for testing
    class TestViewModel(dispatcher: CoroutineDispatcher) : BaseViewModel() {
        init {
            // Replace default coroutineScope with test dispatcher (optional)
            val jobField = BaseViewModel::class.java.getDeclaredField("coroutineScope")
            jobField.isAccessible = true
            jobField.set(this, CoroutineScope(dispatcher + SupervisorJob()))
        }
    }
}