package com.android.template.ui.base.paging

import androidx.databinding.ObservableBoolean
import com.android.template.ui.base.BaseViewModel

abstract class BasePagedListViewModel : BaseViewModel() {

    val screenInitialized = ObservableBoolean()
    val isListEmpty = ObservableBoolean()
}