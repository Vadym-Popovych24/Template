package com.android.template.ui.login.signup

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.android.template.R
import com.android.template.data.remote.api.ApiEndpoints
import com.android.template.databinding.FragmentSignUpBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.signup.viewmodel.SignUpViewModel
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
            signUp()
        }

        binding.btnSignup.setOnClickListenerWithPreValidation(viewModel.formValidator) {
            signUp()
        }

    }

    private fun signUp() {
        viewModel.requestToken(binding.inputEmail.text.toString()) { requestKey ->
            moveToApproveRequestKey("${ApiEndpoints.ENDPOINT_WEB_APPROVE}$requestKey")
        }
        /*viewModel.signUp(
            firstName = binding.inputFirstName.text.toString(),
            lastName = binding.inputLastName.text.toString(),
            email = binding.inputEmail.text.toString(),
            password = binding.inputPassword.text.toString()
        ) {
            viewModel.signUpFinishedCallback?.invoke()
        }*/
    }

    private fun moveToApproveRequestKey(url: String) =
        try {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            })
        } catch (e: ActivityNotFoundException) {
            showToast(R.string.web_view_open_error.getStringFromResource
                .format(url))
        }

    private fun subscribeToObservableFields() {
        viewModel.signUpFinishedCallback = {
            moveToMainActivity()
        }

        viewModel.loadingCallback = { loading ->
            BindingUtils.loadingCircularProgressButton(binding.btnSignup, loading)
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.requestToken.isNullOrEmpty().not()) {
            viewModel.requestToken?.let {
                viewModel.approveToken(
                    requestToken = it,
                    firstName = binding.inputFirstName.text.toString(),
                    lastName = binding.inputLastName.text.toString(),
                    email = binding.inputEmail.text.toString(),
                    password = binding.inputPassword.text.toString()
                )
            }
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