package com.android.template.ui.bottom_menu2

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentBottomMenuTwoBinding
import com.android.template.ui.base.BaseFragmentWithBottomMenu
import com.android.template.ui.bottom_menu2.viewmodel.BottomMenu2ViewModel

class BottomMenu2Fragment: BaseFragmentWithBottomMenu<FragmentBottomMenuTwoBinding, BottomMenu2ViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initDefaultNavigation()
    }

    companion object {
        fun newInstance() = BottomMenu2Fragment()
    }
}