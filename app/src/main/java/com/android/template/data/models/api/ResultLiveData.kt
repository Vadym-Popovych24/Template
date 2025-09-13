package com.android.template.data.models.api

/**
 * Base class which represents result of some async operation
 */
sealed class ResultLiveData<T> {

    /**
     * Convert Result<T> into Result<R>.
     */
    fun <R> map(mapper: ((T) -> R)? = null): ResultLiveData<R> {
        return when (this) {
            is SuccessLiveData<T> -> {
                if (mapper == null) {
                    throw IllegalStateException("Can't map Success<T> result without mapper.")
                } else {
                    SuccessLiveData(mapper(this.value))
                }
            }
            is ErrorLiveData<T> -> ErrorLiveData(this.error)
            is EmptyLiveData<T> -> EmptyLiveData()
            is PendingLiveData<T> -> PendingLiveData()
        }
    }

    fun getValueOrNull(): T? {
        if (this is SuccessLiveData<T>) return this.value
        return null
    }

    fun isFinished() = this is SuccessLiveData<T> || this is ErrorLiveData<T>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (this is SuccessLiveData<*> && other is SuccessLiveData<*>) {
            return this.value == other.value
        } else if (this is ErrorLiveData<*> && other is ErrorLiveData<*>) {
            return this.error == other.error
        }
        return true
    }

    override fun hashCode(): Int {
        if (this is SuccessLiveData<*>) return javaClass.hashCode() +
                31 * this.value.hashCode()
        if (this is ErrorLiveData<*>) return javaClass.hashCode() +
                31 * this.error.hashCode()
        return javaClass.hashCode()
    }

}

class SuccessLiveData<T>(
    val value: T
) : ResultLiveData<T>()

class ErrorLiveData<T>(
    val error: Throwable
) : ResultLiveData<T>()

class EmptyLiveData<T> : ResultLiveData<T>()

class PendingLiveData<T> : ResultLiveData<T>()