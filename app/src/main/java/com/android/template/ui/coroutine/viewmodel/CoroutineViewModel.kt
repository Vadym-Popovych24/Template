package com.android.template.ui.coroutine.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.android.template.ui.base.BaseViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

class CoroutineViewModel @Inject constructor() : BaseViewModel() {

    private val dispatcher: CoroutineDispatcher = Dispatchers.Default

    init {
        viewModelScope.launch {
            Log.e("launch", "ok")
            delay(5000)
            Log.e("delay", "5 second")
        }

        viewModelScope.launch {

            delay(1000)

            val result = withContext(dispatcher) {
                val part1 = async {
                    Log.e("async", "1")
                    delay(1000)
                    return@async "Part 1 done"
                }
                val part2 = async {
                    Log.e("async", "2")
                    delay(1000)
                    return@async "Part 2 done"
                }
                val part3 = async {
                    Log.e("async", "3")
                    delay(1000)
                    return@async "Part 3 done"
                }

                val result1 = part1.await()
                val result2 = part2.await()
                val result3 = part3.await()

                return@withContext "$result1\n$result2\n$result3"
            }

            Log.e("result", result)
        }
    }
}