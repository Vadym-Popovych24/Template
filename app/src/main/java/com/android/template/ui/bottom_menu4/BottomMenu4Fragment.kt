package com.android.template.ui.bottom_menu4

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentBottomMenuFourBinding
import com.android.template.ui.base.BaseFragmentWithBottomMenu
import com.android.template.ui.bottom_menu4.viewmodel.BottomMenu4ViewModel

class BottomMenu4Fragment : BaseFragmentWithBottomMenu<FragmentBottomMenuFourBinding, BottomMenu4ViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initDefaultNavigation()
    }

    companion object {
        fun newInstance() = BottomMenu4Fragment()
    }
}