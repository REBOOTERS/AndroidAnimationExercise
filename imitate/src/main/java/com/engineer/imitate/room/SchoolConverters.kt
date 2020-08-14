package com.engineer.imitate.room

import androidx.room.TypeConverter
import com.engineer.imitate.model.School
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created on 2020/8/14.
 * @author rookie
 */
class SchoolConverters {

    @TypeConverter
    fun stringToObject(json: String): List<School> {
        val gson = Gson()
        val list = gson.fromJson<List<School>>(json, object : TypeToken<List<School>>() {}.type)
        return list
    }

    @TypeConverter
    fun objectToString(list: List<School>): String {
        return Gson().toJson(list)
    }
}