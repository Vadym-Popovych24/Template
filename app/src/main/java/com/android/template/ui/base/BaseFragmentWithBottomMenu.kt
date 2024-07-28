package com.android.template.ui.base

import android.view.View
import androidx.databinding.ViewDataBinding

abstract class BaseFragmentWithBottomMenu<T : ViewDataBinding, V : BaseViewModel> : BaseFragment<T, V>() {

    override val bottomNavigationVisibility: Int
        get() = View.VISIBLE
}