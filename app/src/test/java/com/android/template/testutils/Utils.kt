package com.android.template.testutils

import org.junit.Assert

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