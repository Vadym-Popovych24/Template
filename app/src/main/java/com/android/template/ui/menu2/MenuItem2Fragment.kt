package com.android.template.ui.menu2

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentMenuItemTwoBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.menu2.viewmodel.MenuItem2ViewModel

class MenuItem2Fragment: BaseFragment<FragmentMenuItemTwoBinding, MenuItem2ViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()
    }
}