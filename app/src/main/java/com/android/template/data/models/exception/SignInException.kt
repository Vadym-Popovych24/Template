package com.android.template.data.models.exception

import com.android.template.R
import com.android.template.utils.getStringFromResource
import java.lang.IllegalArgumentException

class SignInException : IllegalArgumentException(
    R.string.invalid_username_or_password.getStringFromResource
)