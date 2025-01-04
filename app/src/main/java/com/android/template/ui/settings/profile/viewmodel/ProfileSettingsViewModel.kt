package com.android.template.ui.settings.profile.viewmodel

import com.android.template.data.models.ProfileSettings
import com.android.template.data.models.enums.GenderType
import com.android.template.data.models.enums.Language
import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import com.rule.validator.formvalidator.FormValidator
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfileSettingsViewModel @Inject constructor(private val profileManager: ProfileManager) :
    BaseViewModel() {

    private lateinit var profileSettings: ProfileSettings
    private var genderCode: Int? = null
    private var languageCode: Int? = null
    private var profileId: Long = 0

    var uploadCallback: (() -> Unit)? = null

    val formValidator = FormValidator(null)

    override fun onCleared() {
        super.onCleared()
        formValidator.clear()
    }

    fun loadData(callback: (ProfileSettings) -> Unit) =
        makeRxInvisible(profileManager.getProfileSettings()) {
            this.profileSettings = it
            this.profileId = it.profileId
            callback.invoke(it)
        }

    fun save(profileSettings: ProfileSettings) {
        makeRx(profileManager.updateProfile(profileSettings).delay(2, TimeUnit.SECONDS)) {
            uploadCallback?.invoke()
        }
    }

    fun setLanguage(language: Language?) {
        this.languageCode = language?.code
    }

    fun getLanguageCode() = languageCode

    fun setGender(genderType: GenderType?) {
        this.genderCode = genderType?.code
    }

    fun getGender() = genderCode

    fun getProfileId() = profileId

}