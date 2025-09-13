package com.android.template.data.models.exception

import com.android.template.data.models.enums.SignUpError
import com.android.template.utils.helpers.ResourceProvider

class SignUpException(val errors: List<SignUpError>, val resourceProvider: ResourceProvider) : IllegalArgumentException(
    errors.joinToString(
        "\n"
    ) {
        resourceProvider.getString(it.errorMessage)
    })