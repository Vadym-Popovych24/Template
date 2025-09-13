package com.android.template.data.models.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.template.data.models.api.model.AccountWithSession
import com.android.template.data.models.api.model.SignUpProfileData

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "profile_id")
    val profileId: Long = 0,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = null,

    @ColumnInfo(name = "gender")
    val gender: Int? = null,

    @ColumnInfo(name = "culture")
    val culture: Int? = null,

    @ColumnInfo(name = "birthday")
    val birthday: String? = null,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "username")
    val userName: String,

    @ColumnInfo(name = "password")
    val password: String,

    @ColumnInfo(name = "avatar_path")
    val avatarPath: String? = null,

    @ColumnInfo(name = "cover_path")
    val coverPath: String? = null,

    @ColumnInfo(name = "request_token")
    val requestToken: String,

    @ColumnInfo(name = "session_id")
    val sessionId: String
) {

    companion object {
        fun mapTo(
            requestToken: String,
            accountWithSession: AccountWithSession,
            signUpProfileData: SignUpProfileData
        ): ProfileEntity {
            return ProfileEntity(
                id = 0, // auto generate id
                profileId = accountWithSession.accountResponse.id,
                email = signUpProfileData.email,
                firstName = signUpProfileData.firstName,
                lastName = signUpProfileData.lastName,
                userName = accountWithSession.accountResponse.username,
                password = signUpProfileData.password,
                avatarPath = accountWithSession.accountResponse.avatar.tmdb.avatarPath,
                requestToken = requestToken,
                sessionId = accountWithSession.sessionId
            )
        }

        fun getEmptyProfileEntity() = ProfileEntity(
            id = 0,
            profileId = 0,
            email = "",
            phoneNumber = null,
            gender = null,
            culture = null,
            birthday = null,
            firstName = "",
            lastName = "",
            userName = "",
            password = "",
            avatarPath = null,
            coverPath = null,
            requestToken = "",
            sessionId = ""
        )
    }
}