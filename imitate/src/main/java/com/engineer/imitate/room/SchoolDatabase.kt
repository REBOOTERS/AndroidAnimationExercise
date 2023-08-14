package com.engineer.imitate.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.engineer.imitate.model.Schools

/**
 * Created on 2020/8/14.
 * @author rookie
 */
@Database(
    entities = [Schools::class],
    version = SchoolDatabase.VERSION,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ],
    exportSchema = true
)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao


    companion object {

        private var INSTANCE: SchoolDatabase? = null


        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `Schools` ADD COLUMN type INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE `Schools` ADD COLUMN count INTEGER NOT NULL DEFAULT 0")
            }
        }

        const val VERSION = 3

        fun getInstance(context: Context): SchoolDatabase {
            if (INSTANCE == null) {
                synchronized(SchoolRepository::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext, SchoolDatabase::class.java, "school-core.db"
                        ).addMigrations(MIGRATION_1_2)
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}