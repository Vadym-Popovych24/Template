package com.android.template.ui.login.reset

import android.os.Bundle
import android.view.View
import com.android.template.R
import com.android.template.databinding.FragmentResetPasswordBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.reset.viewmodel.PasswordResetViewModel
import com.android.template.ui.navigation.NavigationActivity

class PasswordResetFragment :
    BaseFragment<FragmentResetPasswordBinding, PasswordResetViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUI()
    }

    private fun setupViewModel() {
        viewModel.resetPasswordFinishedCallback = {
            requireActivity().finish()
            hideKeyboard()
            moveToActivity(NavigationActivity.newIntent(requireContext()))
        }

        viewModel.actionCallback = {
            hideKeyboard()
        }

        viewModel.currentState.observe(viewLifecycleOwner) {
            if (it == PasswordResetViewModel.State.RESET_PASSWORD) {
                binding.toolbar.initUpNavigation()
            //    binding.clockView?.init(PasswordResetViewModel.SECURE_CODE_ATTEMPT_DURATION)
            } else if (it == PasswordResetViewModel.State.SAVE_NEW_PASSWORD) {
                binding.toolbar.releaseNavigationIcon()
            }

            binding.toolbar.menu?.findItem(R.id.close)?.isVisible =
                it == PasswordResetViewModel.State.SAVE_NEW_PASSWORD

        }
    }

    private fun setupUI() {
        binding.toolbar.initUpNavigation()
        binding.toolbar.inflateMenu(R.menu.menu_close)
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.close) {
                initConfirmationAlert(
                    context!!,
                    R.string.cancel_password_reset_confirmation_message
                ) { navController.navigateUp() }
            }
            true
        }

/*        clockView?.updateTextCallback = {
            R.string.enter_code_timer_hint.getStringFromResource.format(it)
        }

        clockView?.completeCallback = {
            showSendAgainButton()
        }

        email?.setOnActionDoneCallback {
            viewModel.action()
        }

        confirmPassword?.setOnActionDoneCallback {
            viewModel.action()
        }

        codeView?.setOnActionDoneCallback {
            viewModel.action()
        }*/
    }

/*    private fun showSendAgainButton() =
        clockView?.setTextWithClickableLink(
            R.string.enter_code_timer_finished_hint.getStringFromResource,
            R.string.enter_code_timer_finished_clickable.getStringFromResource
        ) {
            viewModel.resendCode {
                clockView?.init(PasswordResetViewModel.SECURE_CODE_ATTEMPT_DURATION)
            }
        }*/

    override fun navigateUp() {
        if (viewModel.currentState.value == PasswordResetViewModel.State.RESET_PASSWORD) {
            viewModel.currentState.value = PasswordResetViewModel.State.SEND_CODE_TO_EMAIL
            viewModel.code.set("")
        } else {
            super.navigateUp()
        }
    }
}