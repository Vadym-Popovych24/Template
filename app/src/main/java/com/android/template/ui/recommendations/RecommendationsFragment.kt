package com.android.template.ui.recommendations

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentRecommendationsBinding
import com.android.template.ui.base.BaseFragmentWithBottomMenu
import com.android.template.ui.recommendations.viewmodel.RecommendationsViewModel

class RecommendationsFragment : BaseFragmentWithBottomMenu<FragmentRecommendationsBinding, RecommendationsViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initDefaultNavigation()
    }
}