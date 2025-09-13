package com.android.template.data.models.db.conversation

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "message_attachments",
    indices = [Index("message_id")],)
class Attachment {

    @PrimaryKey
    var id: String = ""

    @Embedded
    var file: File? = null

    @ColumnInfo(name = "message_id")
    var messageId: String = ""

    class File{

        @SerializedName("id")
        var fileId: String = ""

        var mimeType: String = ""

        var name: String = ""

        var path: String = ""

        var previewPath: String = ""

        var titleAttribute: String = ""

        fun isImage() = mimeType.contains("image")
    }
}