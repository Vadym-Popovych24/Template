package com.android.template.data.local.converter

import android.text.TextUtils
import androidx.room.TypeConverter
import com.android.template.data.models.db.Picture

class PictureConverter {

    @TypeConverter
    fun toString(value: Picture?): String? {
        return value?.altAttribute
    }

    @TypeConverter
    fun toPicture(value: String): Picture? {
        return if (TextUtils.isEmpty(value)) null else Picture(value)
    }

}