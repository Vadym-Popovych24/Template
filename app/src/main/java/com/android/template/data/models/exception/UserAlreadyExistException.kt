package com.android.template.data.models.exception

import com.android.template.R
import com.android.template.utils.getStringFromResource
import java.lang.IllegalArgumentException

class UserAlreadyExistException : IllegalArgumentException(
    R.string.sign_up_duplicate_error_simple.getStringFromResource
)