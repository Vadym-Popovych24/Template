package com.android.template.ui.bottom_menu3

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentBottomMenuThreeBinding
import com.android.template.ui.base.BaseFragmentWithBottomMenu
import com.android.template.ui.bottom_menu3.viewmodel.BottomMenu3ViewModel

class BottomMenu3Fragment: BaseFragmentWithBottomMenu<FragmentBottomMenuThreeBinding, BottomMenu3ViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initDefaultNavigation()
    }

    companion object {
        fun newInstance() = BottomMenu3Fragment()
    }
}