package com.android.template.data.models.db

import androidx.room.Embedded
import androidx.room.Relation

class ProfileAndAvatar {
    @Embedded
    var post: Profile? = null

    @Relation(parentColumn = "id", entityColumn = "profile_id", entity = ProfileAvatar::class)
    var postPictures: List<ProfileAvatar>? = null
}