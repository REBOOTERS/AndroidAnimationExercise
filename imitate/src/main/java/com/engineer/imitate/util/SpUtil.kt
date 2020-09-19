package com.engineer.imitate.util

import android.content.Context
import androidx.preference.PreferenceManager

/**
 * @author rookie
 * @since 08-21-2019
 */
class SpUtil(context: Context) {

    companion object {
        const val KEY_THEME_NIGHT_ON = "key_theme_night_mode_on"
    }

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