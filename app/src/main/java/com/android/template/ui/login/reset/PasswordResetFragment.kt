package com.android.template.ui.login.reset

import android.os.Bundle
import android.view.View
import com.android.template.R
import com.android.template.databinding.FragmentResetPasswordBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.reset.viewmodel.PasswordResetViewModel
import com.android.template.ui.login.reset.viewmodel.PasswordResetViewModel.State
import com.android.template.ui.navigation.NavigationActivity
import com.android.template.utils.getStringFromResource
import com.android.template.utils.isEmail
import com.android.template.utils.isValidPassword
import com.android.template.utils.setOnActionDoneCallback
import com.android.template.utils.setOnActionDoneCallbackWithPreValidation
import com.android.template.utils.setOnClickListenerWithPreValidation
import com.android.template.utils.setTextWithClickableLink
import com.rule.validator.formvalidator.Validator
import com.rule.validator.formvalidator.validatableformitem.TextInputLayoutValidatableFormItem
import com.rule.validator.formvalidator.validatableformitem.ValidationStyle

class PasswordResetFragment :
    BaseFragment<FragmentResetPasswordBinding, PasswordResetViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservableFields()
        setupValidations()
        bindViews()
    }

    private fun subscribeToObservableFields() {
        viewModel.resetPasswordFinishedCallback = {
            requireActivity().finish()
            hideKeyboard()
            moveToActivity(NavigationActivity.newIntent(requireContext()))
        }

        viewModel.actionCallback = {
            hideKeyboard()
        }

        viewModel.currentState.observe(viewLifecycleOwner) {
            if (it == State.RESET_PASSWORD) {
                binding.toolbar.initUpNavigation()
                binding.includeConfirmCode.clockView.init(PasswordResetViewModel.SECURE_CODE_ATTEMPT_DURATION)
            } else if (it == State.SAVE_NEW_PASSWORD) {
                binding.toolbar.releaseNavigationIcon()
            }

            binding.toolbar.menu?.findItem(R.id.close)?.isVisible =
                it == State.SAVE_NEW_PASSWORD

        }
    }

    private fun bindViews() {
        binding.toolbar.initUpNavigation()
        binding.toolbar.inflateMenu(R.menu.menu_close)
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.close) {
                initConfirmationAlert(
                    requireContext(),
                    R.string.cancel_password_reset_confirmation_message
                ) { navController.navigateUp() }
            }
            true
        }

        binding.includeEmail.inputEmail.setOnActionDoneCallbackWithPreValidation(viewModel.emailFormValidator) {
            viewModel.action()
        }
        binding.includeEmail.btnConfirmEmail.setOnClickListenerWithPreValidation(viewModel.emailFormValidator) {
            viewModel.action()
        }
        binding.includeConfirmCode.btnConfirmCode.setOnClickListener {
            validateOtpCode(binding.includeConfirmCode.codeView.text.toString())
        }
        binding.includeConfirmCode.clockView.updateTextCallback = {
            R.string.enter_code_timer_hint.getStringFromResource.format(it)
        }

        binding.includeConfirmCode.clockView.completeCallback = {
            showSendAgainButton()
        }

        binding.includeConfirmCode.codeView.setOnActionDoneCallback {
            validateOtpCode(binding.includeConfirmCode.codeView.text.toString())
        }

        binding.includePassword.inputConfirmPassword.setOnActionDoneCallbackWithPreValidation(viewModel.passwordFormValidator) {
            viewModel.action()
        }
        binding.includePassword.btnLogin.setOnClickListenerWithPreValidation(viewModel.passwordFormValidator) {
            viewModel.action()
        }
    }

    private fun showSendAgainButton() =
        binding.includeConfirmCode.clockView.setTextWithClickableLink(
            R.string.enter_code_timer_finished_hint.getStringFromResource,
            R.string.enter_code_timer_finished_clickable.getStringFromResource
        ) {
            viewModel.resendCode {
                binding.includeConfirmCode.clockView.init(PasswordResetViewModel.SECURE_CODE_ATTEMPT_DURATION)
            }
        }

    private fun setupValidations() {
        viewModel.emailFormValidator.apply {
            clear()

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.includeEmail.inputLayoutEmail,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_email.getStringFromResource,
                            function = { (binding.includeEmail.inputEmail.text.toString().isEmail()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )
        }

        viewModel.passwordFormValidator.apply {
            clear()

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.includePassword.inputLayoutPassword,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_password.getStringFromResource,
                            function = {
                                (binding.includePassword.inputPassword.text.toString().isValidPassword())
                            })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.includePassword.inputLayoutConfirmPassword,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.passwords_not_match.getStringFromResource,
                            function = { (binding.includePassword.inputPassword.text.toString() == binding.includePassword.inputConfirmPassword.text.toString()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )
        }
    }

    private fun validateOtpCode(otpCode: String?) {
        if (otpCode?.length == CODE_LENGTH) {
            viewModel.action()
        } else {
            showToast(R.string.verification_code_rule)
        }
    }

    override fun navigateUp() {
        if (viewModel.currentState.value == State.RESET_PASSWORD) {
            viewModel.currentState.value = State.SEND_CODE_TO_EMAIL
            viewModel.code.set("")
        } else {
            super.navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.currentState.value = State.SEND_CODE_TO_EMAIL
    }

    companion object {
        private const val CODE_LENGTH = 6
    }
}