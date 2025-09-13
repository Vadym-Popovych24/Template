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

        fun getInstance(context: Context, keyEncryptor: KeyEncryptor): TemplateDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context, keyEncryptor).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context, keyEncryptor: KeyEncryptor): TemplateDatabase {
            val dbName = "TemplateDatabase.db"
            val factory = SupportFactory(keyEncryptor.getOrCreateEncryptedKey())

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
    // @RenameTable(fromTableName = "table_name", toTableName = "table_new_name")
    // @DeleteColumn(tableName = "table_name", columnName = "column_name")
    // @RenameColumn(tableName = "table_name", fromColumnName = "column_name", toColumnName = "column_new_name")
    class MyMigration : AutoMigrationSpec

    abstract fun profileDao(): ProfileDao
    abstract fun movieDao(): MovieDao

}