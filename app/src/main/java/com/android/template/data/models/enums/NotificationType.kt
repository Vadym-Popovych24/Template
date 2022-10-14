package com.android.template.data.models.enums

import androidx.annotation.StringRes
import com.android.template.R

enum class NotificationType(
    val code: Int,
    val topic: String,
    val notificationMessage: String?,
    val title: String?,
    val body: String?
) {

    JOB_ALERT(
        0, "JobAlert","notification_type_job_alert",
       "JOB_ALERT_TITLE_LOC_KEY",
       "JOB_ALERT_BODY_LOC_KEY"
    ),
    CONTACTS_REQUEST(
        1,
        "ContactsRequest",
        "notification_type_contacts_request",
        "CONTACTS_REQUEST_TITLE_LOC_KEY",
       "CONTACTS_REQUEST_BODY_LOC_KEY"
    ), //+
    JOB_APPLICATION(
        2,
        "JobApplicant",
       "notification_type_job_applicant",
        null,
        "JOB_APPLICANT_BODY_LOC_KEY"
    ),
    CONTACTS_REQUEST_APPROVED(
        3,
        "ContactsRequestApproved",
        "notification_type_contacts_request_approved",
        "CONTACTS_REQUEST_APPROVED_TITLE_LOC_KEY",
        "CONTACTS_REQUEST_APPROVED_BODY_LOC_KEY"
    ),//+
    NEW_JOBS(
        4,
        "NewJob",
        "notification_type_new_jobs",
        "NEW_JOB_TITLE_LOC_KEY",
        "NEW_JOB_BODY_LOC_KEY"
    ),
    NEW_MESSAGES(
        5,
        "Message",
        "notification_type_new_messages",
        "NEW_MESSAGE_TITLE_LOC_KEY",
        "NEW_MESSAGE_BODY_LOC_KEY"
    );

    companion object {

        fun findByCode(code: Int) = values().firstOrNull { it.code == code }

        fun findByTopic(topic: String) = values().firstOrNull { it.topic == topic }

    }

}