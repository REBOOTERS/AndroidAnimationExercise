package com.engineer.imitate.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.engineer.imitate.model.Schools

/**
 * Created on 2020/8/14.
 * @author rookie
 */
@Database(entities = [Schools::class], version = 1, exportSchema = false)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao

    companion object {
        private var INSTANCE: SchoolDatabase? = null

        fun getInstance(context: Context): SchoolDatabase {
            if (INSTANCE == null) {
                synchronized(SchoolRepository::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            SchoolDatabase::class.java, "school-core.db"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}