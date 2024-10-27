package com.android.template.ui.login.signup

import android.os.Bundle
import android.view.View
import com.android.template.R
import com.android.template.databinding.FragmentSignUpBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.signup.viewmodel.SignUpViewModel
import com.android.template.ui.navigation.NavigationActivity
import com.android.template.utils.BindingUtils
import com.android.template.utils.getStringFromResource
import com.android.template.utils.isEmail
import com.android.template.utils.isValidName
import com.android.template.utils.isValidPassword
import com.android.template.utils.setOnActionDoneCallbackWithPreValidation
import com.android.template.utils.setOnClickListenerWithPreValidation
import com.rule.validator.formvalidator.Validator
import com.rule.validator.formvalidator.validatableformitem.TextInputLayoutValidatableFormItem
import com.rule.validator.formvalidator.validatableformitem.ValidationStyle

class SignUpFragment : BaseFragment<FragmentSignUpBinding, SignUpViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        subscribeToObservableFields()
        setupValidations()
    }

    private fun bindViews() {

        binding.toolbar.initUpNavigation()

        binding.inputConfirmPassword.setOnActionDoneCallbackWithPreValidation(viewModel.formValidator) {
            viewModel.signUp {
                viewModel.signUpFinishedCallback?.invoke()
            }
        }

        binding.btnSignup.setOnClickListenerWithPreValidation(viewModel.formValidator) {
            viewModel.signUp {
                viewModel.signUpFinishedCallback?.invoke()
            }
        }

    }

    private fun subscribeToObservableFields() {
        viewModel.signUpFinishedCallback = {
            requireActivity().finish()
            hideKeyboard()
            moveToActivity(NavigationActivity.newIntent(requireContext()))
        }

        viewModel.loadingCallback = { loading ->
            BindingUtils.loadingCircularProgressButton(binding.btnSignup, loading)
        }
    }

    private fun setupValidations() {
        viewModel.formValidator.apply {
            clear()

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.inputLayoutFirstName,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .minMaxLength(minLength = 2, maxLength = 64, R.string.validation_rule_firstName.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_regex.getStringFromResource,
                            function = { (binding.inputFirstName.text.toString().isValidName()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.inputLayoutLastName,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .minMaxLength(minLength = 2, maxLength = 64, R.string.validation_rule_lastName.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_regex.getStringFromResource,
                            function = { (binding.inputLastName.text.toString().isValidName()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.inputLayoutEmail,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_email.getStringFromResource,
                            function = { (binding.inputEmail.text.toString().isEmail()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.inputLayoutPassword,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_password.getStringFromResource,
                            function = {
                                (binding.inputPassword.text.toString().isValidPassword())
                            })
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
                            function = { (binding.inputPassword.text.toString() == binding.inputConfirmPassword.text.toString()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )
        }
    }
}