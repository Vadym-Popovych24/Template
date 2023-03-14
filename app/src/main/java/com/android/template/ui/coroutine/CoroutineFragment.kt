package com.android.template.ui.coroutine

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.android.template.databinding.FragmentCoroutineBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.coroutine.viewmodel.CoroutineViewModel

class CoroutineFragment : BaseFragment<FragmentCoroutineBinding, CoroutineViewModel>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.tvCoroutine.text = "New Coroutine"
        }, 3000)
    }
}