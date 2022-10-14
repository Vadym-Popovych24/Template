package com.android.template.ui.recommendations.liked

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentLikedBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.recommendations.liked.viewmodel.LikedViewModel
import kotlinx.android.synthetic.main.fragment_liked.*

class LikedFragment: BaseFragment<FragmentLikedBinding, LikedViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.initDefaultNavigation()
    }
}