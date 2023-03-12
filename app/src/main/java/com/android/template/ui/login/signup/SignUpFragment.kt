package com.android.template.ui.login.signup

import android.os.Bundle
import android.view.View
import com.android.template.databinding.FragmentSignUpBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.signup.viewmodel.SignUpViewModel
import com.android.template.ui.navigation.NavigationActivity

class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.signUpFinishedCallback = {
            requireActivity().finish()
            hideKeyboard()
            moveToActivity(NavigationActivity.newIntent(requireContext()))
        }

        binding.toolbar.initUpNavigation()
    }
}