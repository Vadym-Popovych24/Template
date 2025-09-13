package com.android.template.ui.menu1

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentMenuItemOneBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.menu1.viewmodel.MenuItem1ViewModel

class MenuItem1Fragment: BaseFragment<FragmentMenuItemOneBinding, MenuItem1ViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()
    }
}