package com.android.template.data.models.api.model

import com.android.template.data.models.api.response.AccountResponse

data class AccountWithSession(
    val accountResponse: AccountResponse,
    val sessionId: String
)