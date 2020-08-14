package com.engineer.imitate.room

import androidx.room.Dao
import androidx.room.Insert
import com.engineer.imitate.model.Schools

/**
 * Created on 2020/8/14.
 * @author rookie
 */
@Dao
interface SchoolDao {

    @Insert
    fun insert(schools: Schools)
}