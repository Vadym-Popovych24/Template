package com.android.template.ui.recommendations.history

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentHistoryBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.recommendations.history.viewmodel.HistoryViewModel

class HistoryFragment: BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initDefaultNavigation()
    }
}