package com.android.template.ui.recommendations.liked

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentLikedBinding
import com.android.template.ui.base.BaseFragmentWithBottomMenu
import com.android.template.ui.recommendations.liked.viewmodel.LikedViewModel

class LikedFragment: BaseFragmentWithBottomMenu<FragmentLikedBinding, LikedViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initDefaultNavigation()
    }

    companion object {
        fun newInstance() = LikedFragment()
    }
}