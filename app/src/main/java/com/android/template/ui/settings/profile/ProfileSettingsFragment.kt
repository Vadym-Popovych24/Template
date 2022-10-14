package com.android.template.ui.settings.profile

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.android.template.R
import com.android.template.data.models.enums.GenderType
import com.android.template.data.models.enums.Language
import com.android.template.databinding.FragmentProfileSettingsBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.settings.profile.viewmodel.ProfileSettingsViewModel
import com.android.template.utils.getStringFromResource
import com.android.template.utils.helpers.DatePickerCallback
import com.android.template.utils.helpers.defaultDateFormat
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import java.text.ParseException
import java.util.*

class ProfileSettingsFragment :
    BaseFragment<FragmentProfileSettingsBinding, ProfileSettingsViewModel>() {

    private var datePickerDialog: DatePickerDialog? = null
    private val datePickerCallback = DatePickerCallback {
        viewModel.birthDay.set(defaultDateFormat.format(it))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.initUpNavigation()

        viewModel.uploadCallback = onUploadCallback
        initGenderDropdown()
        initLanguageDropdown()

        birthDay?.setOnClickListener {
            showBirthdaySelector()
        }

      //  viewModel.loadData()
    }

    private val onUploadCallback: (() -> Unit)? = {
        showToast(getString(R.string.data_saved))
    }

    private fun initGenderDropdown() = genderDropdown?.apply {
        val genders = GenderType.values()
        val genderLabels = Array(genders.size) {
            genders[it].localizedTitle.getStringFromResource
        }

        val adapter = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, genderLabels)
        setAdapter(adapter)
        setOnItemClickListener { _, _, position, _ ->
            viewModel.setGender(genders[position])
        }
    }

    private fun initLanguageDropdown() = languageDropdown?.apply {
        val languages = Language.values()
        val languageLabels = Array(languages.size) {
            languages[it].localizedTitle.getStringFromResource
        }
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.dropdown_item, languageLabels)
        setAdapter(adapter)
        setOnItemClickListener { _, _, position, _ ->
            viewModel.setLanguage(languages[position])
        }
    }

    private fun showBirthdaySelector() {
        val c = Calendar.getInstance()

        try {
            viewModel.birthDay.get()?.let {
                defaultDateFormat.parse(it)?.let {
                    c.time = it
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
}