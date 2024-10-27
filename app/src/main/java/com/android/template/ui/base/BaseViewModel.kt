package com.android.template.ui.base

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.android.template.R
import com.android.template.data.models.api.ErrorResult
import com.android.template.data.models.api.Result
import com.android.template.data.models.api.SuccessResult
import com.android.template.utils.getStringFromResource
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.FORBIDDEN
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.INTERNAL_SERVER_ERROR
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.INVALID_USERNAME_OR_PASSWORD
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.NOT_FOUND
import com.microsoft.appcenter.utils.HandlerUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject

typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias LiveResult<T> = LiveData<Result<T>>
typealias ResultFlow<T> = Flow<Result<T>>

abstract class BaseViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()

    val isLoading = ObservableBoolean(false)

    lateinit var messageCallback: (message: String?) -> Unit
    var logoutCallback: (() -> Unit)? = null

    private val coroutineContext = SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
        // you can add some exception handling here
    }

    private var job: Job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job + NonCancellable)
    var loadingCallback: ((Boolean) -> Unit)? = null

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
        coroutineScope.cancel()
    }

    fun showMessage(message: String) {
        messageCallback.invoke(message)
    }

    fun showMessage(@StringRes messageId: Int) {
        messageCallback.invoke(messageId.getStringFromResource)
    }

    fun <T> makeRx(single: Single<T>, callback: (T) -> Unit) {
        isLoading.set(true)
        compositeDisposable.add(
            single
                .doOnSubscribe {
                    isLoading.set(true)
                    HandlerUtils.runOnUiThread { loadingCallback?.invoke(true) }
                }
                .doFinally {
                    isLoading.set(false)
                    HandlerUtils.runOnUiThread { loadingCallback?.invoke(false) }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback(it)
                }, {
                    handleError(it)
                })
        )
    }

    fun makeRx(completable: Completable, completeCallback: (() -> Unit)? = null) {
        isLoading.set(true)
        compositeDisposable.add(
            completable
                .doOnSubscribe {
                    isLoading.set(true)
                    HandlerUtils.runOnUiThread { loadingCallback?.invoke(true) }
                }
                .doFinally {
                    isLoading.set(false)
                    HandlerUtils.runOnUiThread { loadingCallback?.invoke(false) }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    completeCallback?.invoke()
                }, {
                    handleError(it)
                })
        )
    }

    fun <T> makeRxInvisible(single: Single<T>, callback: (T) -> Unit) {
        isLoading.set(true)
        compositeDisposable.add(
            single
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback(it)
                }, {})
        )
    }

    fun makeRxInvisible(completable: Completable, completeCallback: (() -> Unit)? = null) {
        isLoading.set(true)
        compositeDisposable.add(
            completable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    completeCallback?.invoke()
                }, {})
        )
    }

    /**
     * Launch the specified suspending [block] and use its result as a value for the
     * provided [liveResult].
     */
    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {
        isLoading.set(true)
        coroutineScope.launch {
            try {
                liveResult.postValue(SuccessResult(block()))
            } catch (e: Exception) {
                if (e !is CancellationException) liveResult.postValue(ErrorResult(e))
            } finally {
                isLoading.set(false)
            }
        }
    }

    protected open fun handleError(it: Throwable) {
        if (it is ANError) {
            when {
                it.errorCode == FORBIDDEN -> {
                    showMessage(R.string.forbidden_error)
                    logoutCallback?.invoke()
                }
                it.errorCode == NOT_FOUND -> {
                    showMessage(R.string.not_found)
                    logoutCallback?.invoke()
                }
                it.errorCode == INTERNAL_SERVER_ERROR -> showMessage(R.string.internal_server_error)
                it.errorCode == INVALID_USERNAME_OR_PASSWORD -> showMessage(R.string.invalid_username_or_password)
                it.errorBody != null && it.errorBody?.isNotEmpty()!! -> {
                    try {
                        val errorBody = JSONObject(it.errorBody)
                        if (errorBody.has("Message")) {
                            showMessage(errorBody.getString("Message"))
                        } else {
                            showMessage(it.errorBody)
                        }
                    } catch (e: Exception) {
                        showMessage(it.errorBody)
                        e.printStackTrace()
                    }
                }
                else -> {
                    if (it.errorCode !=0) messageCallback.invoke(it.errorCode.toString())
                }
            }
        } else {
            it.message?.let{ messageCallback }
        }
        it.printStackTrace()
    }


    protected open fun checkIfEmpty(
        field: ObservableField<String>,
        error: ObservableField<String>,
        length: Int = 1
    ): Boolean {
        val value = field.get()
        return if (value == null || value.length < length) {
            error.set(R.string.invalid_value.getStringFromResource)
            true
        } else {
            error.set(null)
            false
        }
    }
}