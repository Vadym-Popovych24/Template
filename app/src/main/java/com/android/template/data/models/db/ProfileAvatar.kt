package com.android.template.data.models.db

import androidx.room.*

@Entity(tableName = "profile_avatars",
    indices = [Index("profile_id")],
    foreignKeys = [ForeignKey(
        entity = ProfileEntity::class,
        parentColumns = ["id"],
        childColumns = ["profile_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )])
class ProfileAvatar {
    @PrimaryKey
    @ColumnInfo(name = "picture_id")
    var pictureId: Int = 0

    @ColumnInfo(name = "profile_id")
    var profileId: Int = 0

    var picture: String? = null
}