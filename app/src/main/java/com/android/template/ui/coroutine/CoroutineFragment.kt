package com.android.template.ui.coroutine

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentCoroutineBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.coroutine.viewmodel.CoroutineViewModel
import kotlinx.android.synthetic.main.fragment_coroutine.*

class CoroutineFragment : BaseFragment<FragmentCoroutineBinding, CoroutineViewModel>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()
    }
}