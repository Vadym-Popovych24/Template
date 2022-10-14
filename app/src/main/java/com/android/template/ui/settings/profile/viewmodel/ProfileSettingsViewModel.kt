package com.android.template.ui.settings.profile.viewmodel

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.android.template.R
import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.enums.GenderType
import com.android.template.data.models.enums.Language
import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.getStringFromResource
import com.android.template.utils.helpers.convert
import com.android.template.utils.helpers.defaultDateFormat
import com.android.template.utils.helpers.simpleDateFormat
import com.android.template.utils.isEmail
import javax.inject.Inject

class ProfileSettingsViewModel @Inject constructor(private val profileManager: ProfileManager) :
    BaseViewModel() {

    val firstName = ObservableField<String>()
    val firstNameError = ObservableField<String>()

    val lastName = ObservableField<String>()
    val lastNameError = ObservableField<String>()

    val email = ObservableField<String>()
    val emailError = ObservableField<String>()

    val birthDay = ObservableField<String>()
    val birthDayError = ObservableField<String>()

    val phone = ObservableField<String>()
    val phoneError = ObservableField<String>()

    val gender = ObservableField<String>()
    private val genderCode = ObservableInt()
    val language = ObservableField<String>()
    private val languageCode = ObservableInt()

    private lateinit var profileSettings: ProfileSettings

    var uploadCallback: (() -> Unit)? = null

    fun loadData() = makeRx(profileManager.getProfileSettings()) {
        this.profileSettings = it
        showData(profileSettings)
    }

    private fun showData(profileSettings: ProfileSettings) {
        firstName.set(profileSettings.firstName)
        lastName.set(profileSettings.lastName)
        email.set(profileSettings.email)
        phone.set(profileSettings.phoneNumber)
        birthDay.set(
            convert(profileSettings.birthday, simpleDateFormat, defaultDateFormat)
        )

        setGender(GenderType.getGenderByCode(profileSettings.gender))
        setLanguage(Language.getLanguageByCode(profileSettings.culture))
    }

    fun save() {
        if (isDataValid()) {
            makeRx(profileManager.updateProfile(getUpdatedBody())){
                uploadCallback?.invoke()
            }
        }
    }

    private fun getUpdatedBody() = ProfileSettings (
        firstName.get().toString(),
        lastName.get().toString(),
        convert(birthDay.get().toString(), defaultDateFormat, simpleDateFormat),
        email.get().toString(),
        phone.get().toString(),
            firstName.get().toString() +  " " + lastName.get().toString(),
            genderCode.get() ,
            languageCode.get(),
            1
    )

    fun setLanguage(language: Language) {
        this.language.set(language.localizedTitle.getStringFromResource)
        this.languageCode.set(language.code)
    }

    fun setGender(genderType: GenderType) {
        this.gender.set(genderType.localizedTitle.getStringFromResource)
        this.genderCode.set(genderType.code)
    }

    private fun isDataValid(): Boolean {
        if (checkIfEmpty(firstName, firstNameError)) {
            return false
        } else if (checkIfEmpty(lastName, lastNameError)) {
            return false
        } else if (checkIfEmpty(email, emailError) || email.get().toString().isEmail().not()) {
            return false
        } else if (isPhoneValid(phone.get().toString()).not()) {
            return false
        } else if (checkIfEmpty(birthDay, birthDayError)) {
            return false
        }
        return true
    }

    private fun isPhoneValid(phone : String) : Boolean {
        val pattern = "^\\+?[0-9]{8,14}$".toRegex()
        if (!pattern.matches(phone) && !phone.isNullOrEmpty()) {
            phoneError.set(R.string.invalid_phone.getStringFromResource)
              return false
        } else {
            phoneError.set(null)
        }

        return true
    }
}