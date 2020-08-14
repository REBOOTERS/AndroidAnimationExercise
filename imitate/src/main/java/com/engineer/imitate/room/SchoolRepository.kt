package com.engineer.imitate.room

import android.content.Context
import com.engineer.imitate.model.Schools

/**
 * Created on 2020/8/14.
 * @author rookie
 */
class SchoolRepository(context: Context) {
    private var schoolDao: SchoolDao = SchoolDatabase.getInstance(context).schoolDao()

    fun insert(schools: Schools) {
        schoolDao.insert(schools)
    }
}