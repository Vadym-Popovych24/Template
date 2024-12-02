package com.android.template.ui.settings.password.viewmodel

import android.text.TextUtils
import androidx.databinding.ObservableField
import com.android.template.R
import com.android.template.data.models.api.request.ChangePasswordRequest
import com.android.template.manager.interfaces.ProfileManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.getStringFromResource
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(private val profileManager: ProfileManager) :
    BaseViewModel() {

    val oldPassword = ObservableField<String>()
    val oldPasswordError = ObservableField<String>()

    val newPassword = ObservableField<String>()
    val newPasswordError = ObservableField<String>()

    val confirmNewPassword = ObservableField<String>()
    val confirmNewPasswordError = ObservableField<String>()

    var uploadCallback: (() -> Unit)? = null

    fun save() {
        if (isDataValid()) {
            isLoading.set(true)
            compositeDisposable.add(
                profileManager.changePassword(
                    ChangePasswordRequest(
                        newPassword.get().toString(),
                        oldPassword.get().toString()
                    )
                )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        oldPassword.set("")
                        oldPasswordError.set("")
                        newPassword.set("")
                        newPasswordError.set("")
                        confirmNewPassword.set("")
                        confirmNewPasswordError.set("")
                        isLoading.set(false)
                        uploadCallback?.invoke()
                    }, {
                        isLoading.set(false)
                        oldPasswordError.set(R.string.incorrect_password.getStringFromResource)
                    })
            )

        }
    }

    private fun isDataValid(): Boolean {
        if (checkIfEmpty(oldPassword, oldPasswordError)) {
            return false
        } else if (checkIfEmpty(newPassword, newPasswordError)) {
            return false
        } else if (checkIfEmpty(confirmNewPassword, confirmNewPasswordError)) {
            return false
        } else {
            val oldPassword = oldPassword.get()
            val newPassword = newPassword.get()
            val confirmNewPassword = confirmNewPassword.get()
            val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,32}\$".toRegex()

            if (TextUtils.equals(oldPassword, newPassword)) {
                newPasswordError.set(R.string.same_password_error.getStringFromResource)
                return false
            } else {
                newPasswordError.set(null)
            }

            if (!pattern.matches(newPassword.toString())) {
                newPasswordError.set(R.string.new_password_not_valid.getStringFromResource)
                return false
            } else {
                confirmNewPasswordError.set(null)
            }

            if (!TextUtils.equals(newPassword, confirmNewPassword)) {
                confirmNewPasswordError.set(R.string.new_password_does_not_match.getStringFromResource)
                return false
            } else {
                confirmNewPasswordError.set(null)
            }

        }
        return true
    }

}