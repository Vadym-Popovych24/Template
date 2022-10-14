package com.android.template.data.models.enums

import androidx.annotation.StringRes
import com.android.template.R

enum class SignUpError(val code: String, @StringRes val errorMessage: Int) {

    REQUIRES_DIGIT("PasswordRequiresDigit", R.string.sign_up_password_requires_digit_error),
    REQUIRES_UPPER("PasswordRequiresUpper", R.string.sign_up_password_requires_upper_error),
    REQUIRES_LOVER("PasswordRequiresLower", R.string.sign_up_password_requires_lower_error),
    EMAIL_ALREADY_TAKEN("DuplicateUserName", R.string.sign_up_duplicate_error);

    companion object {
        fun getByCode(code: String): SignUpError? = values().firstOrNull { it.code == code }
    }

}