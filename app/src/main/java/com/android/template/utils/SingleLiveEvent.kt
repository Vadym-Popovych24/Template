package com.android.template.utils

import android.os.Looper
import androidx.lifecycle.MutableLiveData

class SingleLiveEvent<T> : MutableLiveData<Event<T>> {

    constructor() : super()

    constructor(initialValue: T?) : super(Event(initialValue))

    fun setLiveValue(value: T?) {
        if (Looper.myLooper() == Looper.getMainLooper())
            setValue(Event(value))
        else
            postLiveValue(value)
    }

    fun postLiveValue(value: T?) {
        postValue(Event(value))
    }

    fun getLiveValue(): T? {
        return super.getValue()?.peekContent()
    }
}