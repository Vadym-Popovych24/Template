package com.android.template.ui.profile

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentProfileBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.profile.viewmodel.ProfileViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()
    }

    companion object {

        fun newInstance() = ProfileFragment()
    }
}