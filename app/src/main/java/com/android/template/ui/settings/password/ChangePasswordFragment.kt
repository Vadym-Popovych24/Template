package com.android.template.ui.settings.password

import android.os.Bundle
import android.view.View
import com.android.template.R
import com.android.template.databinding.FragmentChangePasswordsBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.settings.password.viewmodel.ChangePasswordViewModel
import com.android.template.utils.BindingUtils
import com.android.template.utils.getStringFromResource
import com.android.template.utils.isValidPassword
import com.android.template.utils.setOnClickListenerWithPreValidation
import com.rule.validator.formvalidator.Validator
import com.rule.validator.formvalidator.validatableformitem.TextInputLayoutValidatableFormItem
import com.rule.validator.formvalidator.validatableformitem.ValidationStyle

class ChangePasswordFragment :
    BaseFragment<FragmentChangePasswordsBinding, ChangePasswordViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()
        viewModel.uploadCallback = onUploadCallback

        binding.btnSave.setOnClickListenerWithPreValidation(viewModel.formValidator) {
            viewModel.save(
                oldPassword = binding.inputOldPassword.text.toString(),
                newPassword = binding.inputNewPassword.text.toString()
            )
        }

        viewModel.loadingCallback = { loading ->
            requireActivity().runOnUiThread {
                BindingUtils.loadingCircularProgressButton(binding.btnSave, loading)
            }
        }

        setupValidations()
    }

    private val onUploadCallback: (() -> Unit) = {
        showToast(getString(R.string.password_saved))
    }

    private fun setupValidations() {
        viewModel.formValidator.apply {
            clear()

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.inputLayoutOldPassword,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_password.getStringFromResource,
                            function = {
                                (binding.inputOldPassword.text.toString().isValidPassword())
                            })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.inputLayoutNewPassword,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_password.getStringFromResource,
                            function = { (binding.inputNewPassword.text.toString().isValidPassword()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.inputLayoutConfirmPassword,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.passwords_not_match.getStringFromResource,
                            function = { (binding.inputNewPassword.text.toString() == binding.inputConfirmPassword.text.toString()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )
        }
    }
}