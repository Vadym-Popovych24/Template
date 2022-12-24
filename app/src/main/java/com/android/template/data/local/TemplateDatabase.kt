package com.android.template.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.template.data.local.converter.DateConverter
import com.android.template.data.local.converter.ListStringsConverter
import com.android.template.data.local.converter.PictureConverter
import com.android.template.data.local.dao.*
import com.android.template.data.models.db.*
import com.android.template.data.models.db.conversation.Attachment
import com.android.template.data.models.db.conversation.User

@Database(
    entities = [Profile::class, ProfileAvatar::class, User::class, Attachment::class],
    version = 5, exportSchema = false
)
@TypeConverters(
    DateConverter::class,
    PictureConverter::class,
    ListStringsConverter::class
)
abstract class TemplateDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: TemplateDatabase? = null

        fun getInstance(context: Context): TemplateDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, TemplateDatabase::class.java, "TemplateDatabase.db")
                .fallbackToDestructiveMigration()
                .build()
    }


    abstract fun profileDao(): ProfileDao


}