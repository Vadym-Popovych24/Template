@file:Suppress("UNCHECKED_CAST")

package com.android.template.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelProviderFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @SuppressWarnings("unchecked")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var viewModelProvider: Provider<out ViewModel>? = viewModels[modelClass]
        if (viewModelProvider == null) {
            for ((key, value) in viewModels) {
                if (modelClass.isAssignableFrom(key)) {
                    viewModelProvider = value
                    break
                }
            }
        }
        if (viewModelProvider == null) {
            throw IllegalArgumentException("unknown model class $modelClass")
        }
        try {
            return viewModelProvider.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}