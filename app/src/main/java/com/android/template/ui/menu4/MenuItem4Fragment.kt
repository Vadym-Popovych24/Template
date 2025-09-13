package com.android.template.ui.menu4

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentMenuItemFourBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.menu4.viewmodel.MenuItem4ViewModel

class MenuItem4Fragment: BaseFragment<FragmentMenuItemFourBinding, MenuItem4ViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()
    }
}