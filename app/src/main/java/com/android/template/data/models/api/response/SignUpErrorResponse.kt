package com.android.template.data.models.api.response

import com.android.template.data.models.api.model.SignUpErrorCode
import com.android.template.data.models.enums.SignUpError

class SignUpErrorResponse {
    private var errors: List<SignUpErrorCode> = arrayListOf()

    fun getFormattedErrors(): List<SignUpError> = errors.mapNotNull {
        SignUpError.getByCode(it.code)
    }

}