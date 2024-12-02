package com.android.template.data.models.exception

import com.android.template.R
import com.android.template.utils.getStringFromResource
import java.lang.IllegalArgumentException

class UserNotFoundException : IllegalArgumentException(
    R.string.no_user_with_email_error.getStringFromResource
)