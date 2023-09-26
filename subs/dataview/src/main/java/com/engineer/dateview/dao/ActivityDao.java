package com.engineer.dateview.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.engineer.dateview.model.ActModel;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * @authro: Rookie
 * @since: 2019-06-04
 */
@Dao
public interface ActivityDao {

    @Query("select * from activity_table order by class_name desc")
    Observable<List<ActModel>> getAllActivities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ActModel actModel);

    @Query("select * from activity_table where class_name ==:name")
    ActModel getActivityByName(String name);
}
