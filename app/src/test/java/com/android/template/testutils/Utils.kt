package com.android.template.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.junit.Assert
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun wellDone() {
    // indicates test passed successfully
}

inline fun <reified T : Throwable> catch(block: () -> Unit): T {
    try {
        block()
    } catch (e: Throwable) {
        if (e is T) {
            return e
        } else {
            Assert.fail("Invalid exception type. " +
                    "Expected: ${T::class.java.simpleName}, " +
                    "Actual: ${e.javaClass.simpleName}")
        }
    }
    throw AssertionError("No expected exception")
}

fun <T> LiveData<T>.getOrAwaitValue(): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            data = value
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    if (!latch.await(2, TimeUnit.SECONDS)) throw TimeoutException("LiveData value was never set.")
    return data!!
}


fun createFlowOfProgress(number: Int) = flow {
    for (i in 1..number) {
        emit(i)
    }
}.flowOn(Dispatchers.Default)