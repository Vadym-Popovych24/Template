package com.android.template.ui.home

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentHomeBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.home.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.initDefaultNavigation()
    }
}