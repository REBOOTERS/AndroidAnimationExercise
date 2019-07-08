package com.engineer.gif.revert.internal

import android.util.Log

/**
 * @author rookie
 * @since 07-06-2019
 */
internal class TaskTime {

    private var start = 0L

    val MIL_SECOND = 1000F

    init {
        start = System.currentTimeMillis()
    }

    fun release(name: String) {
        val spend = System.currentTimeMillis() - start
        val second = spend / MIL_SECOND
        val result = String.format("方法: %-20s 耗时 %2.6f second\n", name, second)
        Log.e(TAG, result)
    }

}