package com.android.template.ui.login.viewmodel

import androidx.databinding.ObservableField
import com.android.template.ui.base.BaseViewModel
import com.rule.validator.formvalidator.FormValidator
import io.reactivex.Single
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginViewModel @Inject constructor() : BaseViewModel() {
    val email = ObservableField<String>()
    val password = ObservableField<String>()

    init {
        email.set("vadympopovychn24@gmail.com")
        password.set("1234567qQ")
    }

    val formValidator = FormValidator(null)

    override fun onCleared() {
        super.onCleared()
        formValidator.clear()
    }

    fun login(completableCallback: ((String) -> Unit)) {
        // Implement sign in
        makeRx(Single.just("Login successfully").map {
            it
        }.delay(2, TimeUnit.SECONDS), completableCallback)
    }

}