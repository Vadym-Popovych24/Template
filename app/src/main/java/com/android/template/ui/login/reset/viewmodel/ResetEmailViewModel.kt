package com.android.template.ui.login.reset.viewmodel

import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.rule.validator.formvalidator.FormValidator
import javax.inject.Inject

class ResetEmailViewModel @Inject constructor(private val loginManager: LoginManager) :
    BaseViewModel() {

    val emailFormValidator = FormValidator()


    override fun onCleared() {
        super.onCleared()
        emailFormValidator.clear()
    }

    fun requestSecureCode(email: String, callback: () -> Unit) {
        makeRx(loginManager.requestResetPasswordCode(email), callback)
    }
}