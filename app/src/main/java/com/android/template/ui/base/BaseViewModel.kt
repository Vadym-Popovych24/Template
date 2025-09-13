package com.android.template.ui.base

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.template.R
import com.android.template.data.models.api.ErrorLiveData
import com.android.template.data.models.api.ResultLiveData
import com.android.template.data.models.api.SuccessLiveData
import com.android.template.data.models.exception.ApproveException
import com.android.template.data.models.exception.SignInException
import com.android.template.data.models.exception.UserAlreadyExistException
import com.android.template.data.models.exception.UserNotFoundException
import com.android.template.utils.helpers.ResourceProvider
import com.android.template.utils.helpers.ResourceProviderImpl
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.FORBIDDEN
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.INTERNAL_SERVER_ERROR
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.INVALID_USERNAME_OR_PASSWORD
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.NOT_FOUND
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import org.json.JSONObject
import retrofit2.HttpException

typealias MutableLiveResult<T> = MutableLiveData<ResultLiveData<T>>
typealias LiveResult<T> = LiveData<ResultLiveData<T>>
typealias ResultFlow<T> = Flow<ResultLiveData<T>>

abstract class BaseViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()

    val isLoading = ObservableBoolean(false)

    var messageCallback: ((message: String?) -> Unit)? = null

    private val coroutineContext = SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ ->  // you can add some exception handling here
    }

    protected open val resourceProvider: ResourceProvider = ResourceProviderImpl()

    private var job: Job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job + NonCancellable)
    var loadingCallback: ((Boolean) -> Unit)? = null

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
        coroutineScope.cancel()
    }

    fun showMessage(message: String) {
        messageCallback?.invoke(message)
    }

    fun showMessage(@StringRes messageId: Int) {
        messageCallback?.invoke(resourceProvider.getString(messageId))
    }

    fun <T : Any> makeRx(single: Single<T>, callback: (T) -> Unit) {
        isLoading.set(true)
        compositeDisposable.add(
            single
                .doOnSubscribe {
                    isLoading.set(true)
                    loadingCallback?.invoke(true)
                }
                .doFinally {
                    isLoading.set(false)
                    loadingCallback?.invoke(false)
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
                    loadingCallback?.invoke(true)
                }
                .doFinally {
                    isLoading.set(false)
                    loadingCallback?.invoke(false)
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    completeCallback?.invoke()
                }, {
                    handleError(it)
                })
        )
    }

    fun <T : Any> makeRxInvisible(single: Single<T>, callback: (T) -> Unit) {
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
                liveResult.postValue(SuccessLiveData(block()))
            } catch (e: Exception) {
                if (e !is CancellationException) liveResult.postValue(ErrorLiveData(e))
            } finally {
                isLoading.set(false)
            }
        }
    }

    open fun handleError(it: Throwable) {
        if (it.message?.contains(resourceProvider.getString(R.string.no_address_associated)) == true) {
            showMessage(R.string.no_internet_error)
            return
        }
        if (it is SignInException) showMessage(R.string.invalid_username_or_password)
        if (it is UserNotFoundException) showMessage(R.string.no_user_with_email_error)
        else if (it is ApproveException) {
            showMessage(R.string.request_token_not_approved)
        } else if (it is UserAlreadyExistException) {
            showMessage(R.string.sign_up_duplicate_error_simple)
        }

        if (it is HttpException) {
            when {
                it.code() == FORBIDDEN -> {
                    showMessage(R.string.forbidden_error)
                }
                it.code() == NOT_FOUND -> {
                    showMessage(R.string.not_found)
                }
                it.code() == INTERNAL_SERVER_ERROR -> showMessage(R.string.internal_server_error)
                it.code() == INVALID_USERNAME_OR_PASSWORD -> showMessage(R.string.invalid_username_or_password)
                it.message().isNotEmpty() -> {
                    try {
                        val errorBody = JSONObject(it.message())
                        if (errorBody.has("Message")) {   // To test this line need instrumented  Android test
                            showMessage(errorBody.getString("Message"))
                        } else {
                            showMessage(it.message())
                        }
                    } catch (e: Exception) {
                        showMessage(it.message())
                        e.printStackTrace()
                    }
                }
                else -> {
                    if (it.code() !=0) showMessage(it.code().toString())
                }
            }
        } else {
            it.message?.let{ showMessage(it) }
        }
        it.printStackTrace()
    }
}