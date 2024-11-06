package com.android.template.ui.login

import android.os.Bundle
import android.view.View
import com.android.template.BuildConfig
import com.android.template.R
import com.android.template.databinding.FragmentLoginBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.viewmodel.LoginViewModel
import com.android.template.ui.navigation.NavigationActivity
import com.android.template.utils.BindingUtils
import com.android.template.utils.getStringFromResource
import com.android.template.utils.isEmail
import com.android.template.utils.isValidPassword
import com.android.template.utils.setOnActionDoneCallbackWithPreValidation
import com.android.template.utils.setOnClickListenerWithPreValidation
import com.android.template.utils.toEditable
import com.rule.validator.formvalidator.Validator
import com.rule.validator.formvalidator.validatableformitem.TextInputLayoutValidatableFormItem
import com.rule.validator.formvalidator.validatableformitem.ValidationStyle

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservableFields()
        setupValidations()
        bindViews()
    }

    private fun bindViews() {

        if (BuildConfig.DEBUG) {
            binding.inputEmail.text = "vadympopovychn24@gmail.com".toEditable()
            binding.inputPassword.text = "1234567qQ".toEditable()
        }

        binding.inputPassword.setOnActionDoneCallbackWithPreValidation(viewModel.formValidator) {
            login()
        }

        binding.tvSignUp.setOnClickListener {
            navController.navigate(R.id.signUpFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            navController.navigate(R.id.resetPasswordFragment)
        }

        binding.btnSignIn.setOnClickListenerWithPreValidation(viewModel.formValidator) {
            login()
        }

    }

    private fun login() {
        viewModel.login(
            username = binding.inputEmail.text.toString(),
            password = binding.inputPassword.text.toString()
        ) {
            moveToMainActivity()
        }
    }


    private fun subscribeToObservableFields() {
        viewModel.loadingCallback = { loading ->
            BindingUtils.loadingCircularProgressButton(binding.btnSignIn, loading)
        }
    }

    private fun moveToMainActivity() {

        if (!onlyForRefreshToken()) {
            hideKeyboard()
            moveToActivity(NavigationActivity.newIntent(requireContext()))
        }
        requireActivity().finish()
    }

    private fun onlyForRefreshToken(): Boolean =
        arguments?.getBoolean(EXTRA_ONLY_REFRESH_TOKEN, false) == true

    private fun setupValidations() {
        viewModel.formValidator.apply {
            clear()

            registerValidator(
                TextInputLayoutValidatableFormItem(
                    binding.inputLayoutEmail,
                    Validator.Builder()
                        .requireRule(R.string.validation_rule_required.getStringFromResource)
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_email.getStringFromResource,
                            function = {  (binding.inputEmail.text.toString().isEmail()) })
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
                            function = {  (binding.inputPassword.text.toString().isValidPassword()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )
        }
    }

    companion object {
        private const val EXTRA_ONLY_REFRESH_TOKEN = "extra_only_refresh_token"

        fun initArgs(onlyRefreshToken: Boolean) = Bundle().apply {
            putBoolean(EXTRA_ONLY_REFRESH_TOKEN, onlyRefreshToken)
        }
    }
}