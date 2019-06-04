package com.engineer.dateview;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.engineer.dateview.dao.ActivityDao;
import com.engineer.dateview.model.ActModel;

/**
 * @author: zhuyongging
 * @since: 2019-06-04
 */
@Database(entities = {ActModel.class}, version = 1)
public abstract class ActivityRoomDatabase extends RoomDatabase {
    public abstract ActivityDao mActivityDao();

    private static volatile ActivityRoomDatabase INSTANCE;

    public static ActivityRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ActivityRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ActivityRoomDatabase.class,"activity_database.db")
                            .setJournalMode(JournalMode.TRUNCATE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
