package com.android.template.ui.coroutine.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.template.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoroutineViewModel @Inject constructor() : BaseViewModel() {

    init {

        viewModelScope.launch {
            delay(1000)
            Log.e("delay", "1 second")
        }
    }
}