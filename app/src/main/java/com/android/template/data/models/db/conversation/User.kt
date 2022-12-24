package com.android.template.data.models.db.conversation

import androidx.room.*

@Entity(tableName = "conversation_users",
    indices = [Index("conversation_id")], )
class User {

    @PrimaryKey
    var id: String = ""

    var userId: String = ""

    var profileId: String = ""

    var avatarUrl: String? = null

    var firstName: String = ""

    var lastName: String = ""

    var isUnread: Boolean = false

    var isMainUser: Boolean = false

    @ColumnInfo(name = "conversation_id")
    var conversationId: String? = null

}