package com.android.template.ui.home

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentHomeBinding
import com.android.template.ui.base.BaseFragmentWithBottomMenu
import com.android.template.ui.home.viewmodel.HomeViewModel

class HomeFragment : BaseFragmentWithBottomMenu<FragmentHomeBinding, HomeViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initDefaultNavigation()
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}