package com.android.template.ui.settings.password

import android.os.Bundle
import android.view.View
import com.android.template.R
import com.android.template.databinding.FragmentChangePasswordsBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.settings.password.viewmodel.ChangePasswordViewModel

class ChangePasswordFragment :
    BaseFragment<FragmentChangePasswordsBinding, ChangePasswordViewModel>(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.oldPassword.set("")
        viewModel.oldPasswordError.set("")
        viewModel.newPassword.set("")
        viewModel.newPasswordError.set("")
        viewModel.confirmNewPassword.set("")
        viewModel.confirmNewPasswordError.set("")

        binding.toolbar.initUpNavigation()
        viewModel.uploadCallback = onUploadCallback
    }

    private val onUploadCallback: (() -> Unit) = {
        navigateUp()
        showToast(getString(R.string.password_saved))
    }
}