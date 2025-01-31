package com.android.template.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.android.template.data.local.converter.*
import com.android.template.data.local.dao.*
import com.android.template.data.models.db.*
import com.android.template.data.models.db.conversation.Attachment
import com.android.template.data.models.db.conversation.User

@Database(
    entities = [ProfileEntity::class, ProfileAvatar::class, User::class, Attachment::class,
        MovieEntity::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [AutoMigration(
        from = 2,
        to = 3,
        spec = TemplateDatabase.MyMigration::class
    )]

)
@TypeConverters(
    DateConverter::class,
    PictureConverter::class,
    ListStringsConverter::class,
    ListIntConverter::class
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


    // Here you write your migration and use @DeleteTable or @RenameColumn
    @DeleteTable(tableName = "contact_requests")
    class MyMigration : AutoMigrationSpec

    abstract fun profileDao(): ProfileDao
    abstract fun movieDao(): MovieDao


}