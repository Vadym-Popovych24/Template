package com.android.template.data.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
class Profile {
    @PrimaryKey
    var id: Int = 0

    @ColumnInfo(name = "profile_id")
    var profileId: Int = 0

    @ColumnInfo(name = "user_name")
    var userName: String? = null

    var email: String? = null

    @ColumnInfo(name = "is_email_verified")
    var isEmailVerified: Boolean = false

    @ColumnInfo(name = "first_name")
    var firstName: String? = null

    @ColumnInfo(name = "last_name")
    var lastName: String? = null

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = null

    @ColumnInfo(name = "is_employee")
    var isEmployee: Boolean = false

    var position: String? = null

    var culture: Int = 0

    var coverPicture: String? = null
}