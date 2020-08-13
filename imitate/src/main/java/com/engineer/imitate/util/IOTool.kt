package com.engineer.imitate.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

/**
 * Created on 2020/8/12.
 *
 * @author rookie
 */
object IOTool {
    fun readStrFromAssets(
        filename: String?,
        mContext: Context
    ): String {
        var result = ""
        try {
            val mInputStream = mContext?.assets.open(filename!!)
            val sb = StringBuilder()
            val buffer = ByteArray(mInputStream.available())
            var len: Int
            while (mInputStream.read(buffer).also { len = it } != -1) {
                val str = String(buffer, 0, len)
                sb.append(str)
            }
            result = sb.toString()
            mInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    fun <T> parse(json: String?, t: T): List<T> {
        val gson = Gson()
        return gson.fromJson(
            json,
            object : TypeToken<List<T>?>() {}.type
        )
    }
}