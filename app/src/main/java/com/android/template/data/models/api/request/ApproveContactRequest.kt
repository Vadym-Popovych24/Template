package com.android.template.data.models.api.request

class ApproveContactRequest (
    val id: Int?,
    val profileId: Int?,
    val resumeId: Int?,
    val resumeName: String? = null,
    val companyId: Int?,
    val companyName: String?= null,
    val status: Int?,
    val comment: String?= null,
    val createdDate: String?= null,
    val returnUrl: String?= null,
    val receiverId: String?= null
)