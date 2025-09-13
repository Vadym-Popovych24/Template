package com.android.template.data.models.api.response

import com.android.template.data.models.enums.ReturnResult

data class ResponseEmptyButNeedResult (val result: ReturnResult, val errorMessage: String? = null)