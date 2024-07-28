package com.android.template.ui.settings

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentSettingsBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.LoginActivity
import com.android.template.ui.settings.password.ChangePasswordFragment
import com.android.template.ui.settings.profile.ProfileSettingsFragment
import com.android.template.ui.settings.viewmodel.SettingsViewModel

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()

        binding.profileSettings.setOnClickListener {
            showFragment(ProfileSettingsFragment())
        }

        binding.passwordSettings.setOnClickListener {
            showFragment(ChangePasswordFragment())
        }

        binding.signOut.setOnClickListener {
            viewModel.logOut {
                requireActivity().finish()
                startActivity(LoginActivity.newIntent(requireContext()))
            }
        }
    }
}