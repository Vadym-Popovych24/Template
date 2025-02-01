package com.android.template.data.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.android.template.data.local.converter.*
import com.android.template.data.local.dao.*
import com.android.template.data.models.db.*
import com.android.template.data.models.db.conversation.Attachment
import com.android.template.data.models.db.conversation.User
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [ProfileEntity::class, ProfileAvatar::class, User::class, Attachment::class,
        MovieEntity::class],
    version = 10,
    exportSchema = true,
    autoMigrations = [AutoMigration(
        from = 9,
        to = 10,
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

        private fun buildDatabase(context: Context): TemplateDatabase {
            val dbName = "TemplateDatabase.db"
            val password = "your_secure_password"
            val factory = SupportFactory(password.toByteArray())

            return Room.databaseBuilder(
                context.applicationContext,
                TemplateDatabase::class.java,
                dbName
            )
                .openHelperFactory(factory)
                .build()
        }
    }


   // Here you can write your migration and use @DeleteTable or @RenameColumn
   // @DeleteTable(tableName = "table_name")
   // @RenameColumn(tableName = "table_name", fromColumnName = "column_name", toColumnName = "column_name")
   class MyMigration : AutoMigrationSpec

    abstract fun profileDao(): ProfileDao
    abstract fun movieDao(): MovieDao


}