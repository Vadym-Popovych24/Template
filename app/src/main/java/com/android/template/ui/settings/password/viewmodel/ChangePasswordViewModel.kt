package com.android.template.ui.settings.password.viewmodel

import com.android.template.R
import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import com.rule.validator.formvalidator.FormValidator
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(private val profileManager: ProfileManager) :
    BaseViewModel() {

    var uploadCallback: (() -> Unit)? = null

    val formValidator = FormValidator(null)

    override fun onCleared() {
        super.onCleared()
        formValidator.clear()
    }

    fun save(oldPassword: String, newPassword: String) {
        makeRx(
            profileManager.changePassword(
                oldPassword = oldPassword,
                newPassword = newPassword
            ).delay(2, TimeUnit.SECONDS)
        ) { rowsUpdated ->
            if (rowsUpdated > 0) {
                uploadCallback?.invoke()
            } else {
                showMessage(R.string.wrong_old_password_error)
            }
        }
    }

}