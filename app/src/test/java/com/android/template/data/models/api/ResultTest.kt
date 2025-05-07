package com.android.template.data.models.api

import com.android.template.testutils.catch
import com.android.template.testutils.wellDone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {

    @Test
    fun getValueOrNullReturnsNullForNonSuccessResults() {
        val emptyLivaDataResult = EmptyLiveData<String>()
        val pendingLiveDataResult = PendingLiveData<String>()
        val errorLivaDataResult = ErrorLiveData<String>(Exception())

        val emptyValue = emptyLivaDataResult.getValueOrNull()
        val pendingValue = pendingLiveDataResult.getValueOrNull()
        val errorValue = errorLivaDataResult.getValueOrNull()

        assertNull(emptyValue)
        assertNull(pendingValue)
        assertNull(errorValue)
    }

    @Test
    fun getValueOrNullReturnsValueForSuccessResult() {
        val successLivaDataResult = SuccessLiveData("test")

        val value = successLivaDataResult.getValueOrNull()

        assertEquals("test", value)
    }

    @Test
    fun isFinishedForSuccessAndErrorReturnsTrue() {
        val errorLivaDataResult = ErrorLiveData<String>(Exception())
        val successLivaDataResult = SuccessLiveData("test")

        val isErrorFinished = errorLivaDataResult.isFinished()
        val isSuccessFinished = successLivaDataResult.isFinished()

        assertTrue(isErrorFinished)
        assertTrue(isSuccessFinished)
    }

    @Test
    fun isFinishedForEmptyAndPendingReturnsFalse() {
        val emptyLivaDataResult = EmptyLiveData<String>()
        val pendingLiveDataResult = PendingLiveData<String>()

        val isEmptyFinished = emptyLivaDataResult.isFinished()
        val isPendingFinished = pendingLiveDataResult.isFinished()

        assertFalse(isEmptyFinished)
        assertFalse(isPendingFinished)
    }

    @Test
    fun testNonSuccessResultsMapping() {
        val exception = Exception()
        val emptyLivaDataResult = EmptyLiveData<String>()
        val pendingLiveDataResult = PendingLiveData<String>()
        val errorLivaDataResult = ErrorLiveData<String>(exception)

        val mappedEmptyResult = emptyLivaDataResult.map<Int>()
        val mappedPendingResult = pendingLiveDataResult.map<Int>()
        val mappedErrorResult = errorLivaDataResult.map<Int>()

        assertTrue(mappedEmptyResult is EmptyLiveData<Int>)
        assertTrue(mappedPendingResult is PendingLiveData<Int>)
        assertTrue(mappedErrorResult is ErrorLiveData<Int>)
        assertSame(exception, (mappedErrorResult as ErrorLiveData<Int>).error)
    }

    @Test
    fun mapWithoutMapperCantConvertSuccessResult() {
        val result = SuccessLiveData("test")

        catch<IllegalStateException> { result.map<Int>() }

        wellDone()
    }

    @Test
    fun mapWithMapperConvertsSuccessToSuccess() {
        val result = SuccessLiveData("123")

        val mappedResult = result.map { it.toInt() }

        assertTrue(mappedResult is SuccessLiveData<Int>)
        assertEquals(123, (mappedResult as SuccessLiveData<Int>).value)
    }

    @Test
    fun testEquals() {
        val exception = IllegalStateException()
        val pendingLiveData1 = PendingLiveData<String>()
        val pendingLiveData2 = PendingLiveData<String>()
        val emptyLivaData1 = EmptyLiveData<String>()
        val emptyLivaData2 = EmptyLiveData<String>()
        val errorLivaData1 = ErrorLiveData<String>(exception)
        val errorLivaData2 = ErrorLiveData<String>(exception)
        val successLivaData1 = SuccessLiveData("val")
        val successLivaData2 = SuccessLiveData("val")

        assertEquals(pendingLiveData1, pendingLiveData2)
        assertEquals(emptyLivaData1, emptyLivaData2)
        assertEquals(errorLivaData1, errorLivaData2)
        assertEquals(successLivaData1, successLivaData2)
    }

    @Test
    fun testNotEquals() {
        val pendingLiveData = PendingLiveData<String>()
        val emptyLivaData = EmptyLiveData<String>()
        val errorLivaData1 = ErrorLiveData<String>(IllegalStateException())
        val errorLivaData2 = ErrorLiveData<String>(IllegalStateException())
        val successLivaData1 = SuccessLiveData("val1")
        val successLivaData2 = SuccessLiveData("val2")

        assertNotEquals(pendingLiveData, emptyLivaData)
        assertNotEquals(pendingLiveData, errorLivaData1)
        assertNotEquals(pendingLiveData, successLivaData1)
        assertNotEquals(emptyLivaData, errorLivaData1)
        assertNotEquals(emptyLivaData, successLivaData1)
        assertNotEquals(errorLivaData1, errorLivaData2)
        assertNotEquals(errorLivaData1, successLivaData1)
        assertNotEquals(successLivaData1, successLivaData2)
    }

    @Test
    fun testHashCode() {
        val exception = IllegalStateException()
        val pendingLiveData1 = PendingLiveData<String>()
        val pendingLiveData2 = PendingLiveData<String>()
        val emptyLivaData1 = EmptyLiveData<String>()
        val emptyLivaData2 = EmptyLiveData<String>()
        val errorLivaData1 = ErrorLiveData<String>(exception)
        val errorLivaData2 = ErrorLiveData<String>(exception)
        val successLivaData1 = SuccessLiveData("val")
        val successLivaData2 = SuccessLiveData("val")

        assertEquals(pendingLiveData1.hashCode(), pendingLiveData2.hashCode())
        assertEquals(emptyLivaData1.hashCode(), emptyLivaData2.hashCode())
        assertEquals(errorLivaData1.hashCode(), errorLivaData2.hashCode())
        assertEquals(successLivaData1.hashCode(), successLivaData2.hashCode())
    }

}