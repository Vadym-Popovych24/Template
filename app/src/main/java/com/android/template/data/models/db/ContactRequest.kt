package com.android.template.data.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.template.data.models.api.request.ApproveContactRequest

@Entity(tableName = "contact_requests")
class ContactRequest {

    @PrimaryKey
    @ColumnInfo
    var id: Int? = null

    @ColumnInfo(name = "profile_id")
    var profileId: Int? = null

    @ColumnInfo(name = "resume_id")
    var resumeId: Int? = null

    @ColumnInfo(name = "resume_name")
    var resumeName: String? = null

    @ColumnInfo(name = "company_id")
    var companyId: Int? = null

    @ColumnInfo(name = "company_name")
    var companyName: String? = null

    @ColumnInfo
    var status: Int? = null

    @ColumnInfo
    var comment: String? = null

    @ColumnInfo
    var description: String? = null

    @ColumnInfo(name = "create_date")
    var createdDate: String? = null

    @ColumnInfo(name = "return_url")
    var returnUrl: String? = null

    @ColumnInfo(name = "receiver_id")
    var receiverId: String? = null

    @ColumnInfo(name = "company_img")
    var companyImg: String? = null

    fun convertContactRequestFromDB() : ApproveContactRequest {
        return ApproveContactRequest(
            id = this@ContactRequest.id,
            profileId = this@ContactRequest.profileId,
            resumeId = this@ContactRequest.resumeId,
            resumeName = this@ContactRequest.resumeName,
            companyId = this@ContactRequest.companyId,
            companyName = this@ContactRequest.companyName,
            status = this@ContactRequest.status,
            comment = this@ContactRequest.comment,
            createdDate = this@ContactRequest.createdDate,
            returnUrl = this@ContactRequest.returnUrl,
            receiverId = this@ContactRequest.receiverId
        )
        }

    }
