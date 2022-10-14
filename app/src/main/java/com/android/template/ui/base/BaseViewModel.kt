package com.android.template.ui.base

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.android.template.R
import com.android.template.utils.getStringFromResource
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.FORBIDDEN
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.INTERNAL_SERVER_ERROR
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.INVALID_USERNAME_OR_PASSWORD
import com.android.template.utils.interceptors.ErrorHandlerInterceptor.Companion.NOT_FOUND
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

abstract class BaseViewModel : ViewModel() {
    val compositeDisposable = CompositeDisposable()

    val isLoading = ObservableBoolean(false)

    lateinit var messageCallback: (message: String?) -> Unit
    var logoutCallback: (() -> Unit)? = null

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     */

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
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
            single.doFinally {
                isLoading.set(false)
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
            completable.doFinally {
                isLoading.set(false)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    completeCallback?.invoke()
                }, {
                    handleError(it)
                })
        )
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