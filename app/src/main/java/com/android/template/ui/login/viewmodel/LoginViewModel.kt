package com.android.template.ui.login.viewmodel

import androidx.databinding.ObservableField
import com.android.template.R
import com.android.template.manager.interfaces.LoginManager
import com.android.template.ui.base.BaseViewModel
import com.android.template.utils.getStringFromResource
import com.android.template.utils.getValueOrEmpty
import com.android.template.utils.isEmail
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginManager: LoginManager) : BaseViewModel() {
    val email = ObservableField<String>()
    val emailError = ObservableField<String>()
    val password = ObservableField<String>()
    val passwordError = ObservableField<String>()
    var authCompleteCallback: (() -> Unit)? = null

    init {
        email.set("vadympopovychn24@gmail.com")
        password.set("1")
    }

    fun loginClick() {
        if (isCredentialsValid()) {
            authCompleteCallback?.invoke()
        }
    }

    private fun isCredentialsValid(): Boolean {
        val email = email.getValueOrEmpty()
        emailError.set("")
        return if (!email.isEmail()) {
            emailError.set(R.string.invalid_value.getStringFromResource)
            false
        } else !checkIfEmpty(password, passwordError)
    }

}