package com.android.template.ui.menu3

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentMenuItemThreeBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.menu3.viewmodel.MenuItem3ViewModel
import kotlinx.android.synthetic.main.fragment_liked.*

class MenuItem3Fragment: BaseFragment<FragmentMenuItemThreeBinding, MenuItem3ViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.initUpNavigation()
    }
}