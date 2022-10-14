package com.android.template.data.models.exception

import com.android.template.data.models.enums.SignUpError
import com.android.template.utils.getStringFromResource
import java.lang.IllegalArgumentException

class SignUpException(val errors: List<SignUpError>) : IllegalArgumentException(
    errors.joinToString(
        "\n"
    ) {
        it.errorMessage.getStringFromResource
    })