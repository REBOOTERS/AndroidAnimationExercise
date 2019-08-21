package com.engineer.imitate.util

import android.content.Context
import android.preference.PreferenceManager

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 08-21-2019
 */
class SpUtil(context: Context) {

    private val sp = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveString(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return sp.getString(key, "")
    }

    fun saveBool(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    fun getBool(key: String): Boolean {
        return sp.getBoolean(key, false)
    }
}