package com.android.template.ui.login.reset

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.template.R
import com.android.template.databinding.FragmentResetConfirmationCodeBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.reset.viewmodel.ResetConfirmationCodeViewModel
import com.android.template.utils.getStringFromResource
import com.android.template.utils.setOnActionDoneCallback
import com.android.template.utils.setTextWithClickableLink

class ResetConfirmationCodeFragment : BaseFragment<FragmentResetConfirmationCodeBinding, ResetConfirmationCodeViewModel>() {

    private val args by navArgs<ResetConfirmationCodeFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        binding.toolbar.initUpNavigation()
        startTimer()

        binding.includeConfirmCode.btnConfirmCode.setOnClickListener {
            validateOtpCode(binding.includeConfirmCode.codeView.text.toString())
        }

        binding.includeConfirmCode.codeView.setOnActionDoneCallback {
            validateOtpCode(binding.includeConfirmCode.codeView.text.toString())
        }

        binding.includeConfirmCode.clockView.updateTextCallback = {
            R.string.enter_code_timer_hint.getStringFromResource.format(it)
        }

        binding.includeConfirmCode.clockView.completeCallback = {
            showSendAgainButton()
        }
    }

    private fun startTimer() {
        binding.includeConfirmCode.clockView.init(ResetConfirmationCodeViewModel.SECURE_CODE_ATTEMPT_DURATION)
    }

    private fun validateOtpCode(otpCode: String?) {
        if (otpCode?.length == CODE_LENGTH) {
            viewModel.confirmSecureCode(
                email = args.email,
                code = binding.includeConfirmCode.codeView.text.toString()
            ) {
                ResetConfirmationCodeFragmentDirections.actionFromConfirmationCodeToResetPassword(
                    args.email,
                    binding.includeConfirmCode.codeView.text.toString()
                ).run {
                    findNavController().navigate(this)
                }
            }
        } else {
            showToast(R.string.verification_code_rule)
        }
    }

    private fun showSendAgainButton() =
        binding.includeConfirmCode.clockView.setTextWithClickableLink(
            R.string.enter_code_timer_finished_hint.getStringFromResource,
            R.string.enter_code_timer_finished_clickable.getStringFromResource
        ) {
            viewModel.resendCode(args.email) {
                startTimer()
            }
        }

    companion object {
        private const val CODE_LENGTH = 6
    }
}