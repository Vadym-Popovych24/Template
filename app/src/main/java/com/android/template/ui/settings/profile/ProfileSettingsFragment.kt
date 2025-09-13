package com.android.template.ui.settings.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.android.template.R
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.enums.GenderType
import com.android.template.data.models.enums.Language
import com.android.template.databinding.FragmentProfileSettingsBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.settings.profile.viewmodel.ProfileSettingsViewModel
import com.android.template.utils.BindingUtils
import com.android.template.utils.getStringFromResource
import com.android.template.utils.helpers.DatePickerCallback
import com.android.template.utils.helpers.convert
import com.android.template.utils.helpers.defaultDateFormat
import com.android.template.utils.helpers.simpleDateFormat
import com.android.template.utils.isEmail
import com.android.template.utils.isPhoneNumber
import com.android.template.utils.isValidName
import com.android.template.utils.setOnClickListenerWithPreValidation
import com.android.template.utils.toEditable
import com.rule.validator.formvalidator.Validator
import com.rule.validator.formvalidator.validatableformitem.TextInputLayoutValidatableFormItem
import com.rule.validator.formvalidator.validatableformitem.ValidationStyle
import java.text.ParseException
import java.util.*

class ProfileSettingsFragment :
    BaseFragment<FragmentProfileSettingsBinding, ProfileSettingsViewModel>() {

    private var datePickerDialog: DatePickerDialog? = null
    private val datePickerCallback = DatePickerCallback {
        binding.inputBirthDay.text = defaultDateFormat.format(it).toEditable()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.initUpNavigation()

        viewModel.uploadCallback = onUploadCallback
        initGenderDropdown()
        initLanguageDropdown()

        binding.inputBirthDay.setOnClickListener {
            showBirthdaySelector()
        }

        viewModel.loadingCallback = { loading ->
            requireActivity().runOnUiThread {
                BindingUtils.loadingCircularProgressButton(binding.btnSave, loading)
            }
        }

        setupValidations()
        viewModel.loadData { profileSettings ->
            showData(profileSettings)
        }

        binding.btnSave.setOnClickListenerWithPreValidation(viewModel.formValidator) {
            viewModel.save(
                ProfileSettings(
                    firstName = binding.inputFirstName.text.toString(),
                    lastName = binding.inputLastName.text.toString(),
                    birthday = binding.inputBirthDay.text.toString(),
                    email = binding.inputEmail.text.toString(),
                    phoneNumber = binding.inputPhone.text.toString(),
                    gender = GenderType.getGenderByCode(viewModel.getGender() ?: -1).code,
                    culture = viewModel.getLanguageCode()
                        ?.let { Language.getLanguageByCode(it).code },
                    profileId = viewModel.getProfileId()
                )
            )
        }

    }

    private fun showData(profileSettings: ProfileSettings) {
        binding.apply {
            inputFirstName.text = profileSettings.firstName.toEditable()
            inputLastName.text = profileSettings.lastName.toEditable()
            inputEmail.text = profileSettings.email.toEditable()
            inputPhone.text = profileSettings.phoneNumber?.toEditable()
            inputBirthDay.text = convert(profileSettings.birthday, simpleDateFormat, defaultDateFormat).toEditable()
            inputGenderDropdown.text = profileSettings.gender?.let { GenderType.getGenderByCode(it).localizedTitle.getStringFromResource.toEditable() }
            inputLanguageDropdown.text = profileSettings.culture?.let {  Language.getLanguageByCode(it).localizedTitle.getStringFromResource.toEditable() }
        }
        viewModel.setGender(profileSettings.gender?.let { gender ->
            GenderType.getGenderByCode(gender)
        })
        viewModel.setLanguage(profileSettings.culture?.let { culture ->
            Language.getLanguageByCode(culture)
        })
    }

    private val onUploadCallback: (() -> Unit) = {
        showToast(getString(R.string.data_saved))
    }

    private fun initGenderDropdown() = binding.inputGenderDropdown.apply {
        val genders = GenderType.entries.toTypedArray()
        val genderLabels = Array(genders.size) {
            genders[it].localizedTitle.getStringFromResource
        }

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, genderLabels)
        setAdapter(adapter)
        setOnItemClickListener { _, _, position, _ ->
            viewModel.setGender(genders[position])
        }
    }

    private fun initLanguageDropdown() = binding.inputLanguageDropdown.apply {
        val languages = Language.entries.toTypedArray()
        val languageLabels = Array(languages.size) {
            languages[it].localizedTitle.getStringFromResource
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, languageLabels)
        setAdapter(adapter)
        setOnItemClickListener { _, _, position, _ ->
            viewModel.setLanguage(languages[position])
        }
    }

    private fun showBirthdaySelector() {
        val c = Calendar.getInstance()

        try {
            binding.inputBirthDay.text.toString().let {
                defaultDateFormat.parse(it)?.let { birthDayDate ->
                    c.time = birthDayDate
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        if (datePickerDialog != null) {
            datePickerDialog?.updateDate(year, month, day)
        } else {
            datePickerDialog =
                DatePickerDialog(requireActivity(), datePickerCallback, year, month, day)
            datePickerDialog?.setOnShowListener { hideKeyboard() }
            datePickerDialog?.setOnDismissListener { hideKeyboard() }
            datePickerDialog?.setOnCancelListener { hideKeyboard() }
        }
        datePickerDialog?.show()
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
                    binding.inputLayoutPhone,
                    Validator.Builder()
                        .customRule(isRequired = true,
                            errorString = R.string.validation_rule_phone.getStringFromResource,
                            function = { (binding.inputPhone.text.toString().isPhoneNumber() || binding.inputPhone.text.toString().isEmpty()) })
                        .build(), ValidationStyle.ON_FOCUS_LOST
                )
            )
        }
    }
}