package com.android.template.ui.recommendations.history

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentHistoryBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.recommendations.history.viewmodel.HistoryViewModel
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment: BaseFragment<FragmentHistoryBinding, HistoryViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.initDefaultNavigation()
    }
}