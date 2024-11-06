package com.android.template.ui.login.viewmodel

import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.rule.validator.formvalidator.FormValidator
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginManager: LoginManager) : BaseViewModel() {

    val formValidator = FormValidator(null)

    override fun onCleared() {
        super.onCleared()
        formValidator.clear()
    }

    fun login(username: String, password: String, completableCallback: (() -> Unit)) {
        // Implement sign in
        makeRx(loginManager.auth(username, password).delay(2, TimeUnit.SECONDS), completableCallback)
    }

}