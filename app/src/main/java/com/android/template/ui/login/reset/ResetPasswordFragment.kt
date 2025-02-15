package com.android.template.ui.login.reset

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.android.template.R
import com.android.template.databinding.FragmentResetPasswordBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.login.reset.viewmodel.ResetPasswordViewModel
import com.android.template.utils.getStringFromResource
import com.android.template.utils.isValidPassword
import com.android.template.utils.setOnActionDoneCallbackWithPreValidation
import com.android.template.utils.setOnClickListenerWithPreValidation
import com.google.firebase.messaging.FirebaseMessaging
import com.rule.validator.formvalidator.Validator
import com.rule.validator.formvalidator.validatableformitem.TextInputLayoutValidatableFormItem
import com.rule.validator.formvalidator.validatableformitem.ValidationStyle

class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding, ResetPasswordViewModel>() {

    private val args by navArgs<ResetPasswordFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupValidations()
        bindViews()
    }

    private fun bindViews() {
        binding.toolbar.initUpNavigation()

        binding.includePassword.inputConfirmPassword.setOnActionDoneCallbackWithPreValidation(viewModel.passwordFormValidator) {
            savePassword()
        }
        binding.includePassword.btnLogin.setOnClickListenerWithPreValidation(viewModel.passwordFormValidator) {
            savePassword()
        }
    }

    private fun savePassword() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                it.result?.let { token ->
                    viewModel.saveFCMToken(token) {
                        viewModel.saveNewPassword(
                            email = args.email,
                            code = args.code,
                            password = binding.includePassword.inputPassword.text.toString()
                        ) {
                            moveToMainActivity()
                        }
                    }
                }
            } else {
                viewModel.loadingCallback?.invoke(false)
                showToast(it.exception?.message.toString())
            }
        }
    }

    private fun setupValidations() {

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

}