package com.android.template.ui.login.reset

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.template.R
import com.android.template.databinding.FragmentResetEmailBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.reset.viewmodel.ResetEmailViewModel
import com.android.template.utils.getStringFromResource
import com.android.template.utils.isEmail
import com.android.template.utils.setOnActionDoneCallbackWithPreValidation
import com.android.template.utils.setOnClickListenerWithPreValidation
import com.rule.validator.formvalidator.Validator
import com.rule.validator.formvalidator.validatableformitem.TextInputLayoutValidatableFormItem
import com.rule.validator.formvalidator.validatableformitem.ValidationStyle

class ResetEmailFragment : BaseFragment<FragmentResetEmailBinding, ResetEmailViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupValidations()
        bindViews()
    }

    private fun bindViews() {
        binding.toolbar.initUpNavigation()

        binding.includeEmail.inputEmail.setOnActionDoneCallbackWithPreValidation(viewModel.emailFormValidator) {
            viewModel.requestSecureCode(binding.includeEmail.inputEmail.text.toString()) {
                moveToResetCode()
            }
        }
        binding.includeEmail.btnConfirmEmail.setOnClickListenerWithPreValidation(viewModel.emailFormValidator) {
            viewModel.requestSecureCode(binding.includeEmail.inputEmail.text.toString()) {
                moveToResetCode()
            }
        }
    }

    private fun moveToResetCode() {
        ResetEmailFragmentDirections.actionFromRestEmailToConfirmationCode(binding.includeEmail.inputEmail.text.toString())
            .run {
                findNavController().navigate(this)
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
                            function = {
                                (binding.includeEmail.inputEmail.text.toString().isEmail())
                            }
                        )
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )
        }
    }
}