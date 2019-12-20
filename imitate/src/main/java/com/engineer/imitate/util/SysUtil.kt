package com.engineer.imitate.util

import android.content.Context
import android.os.Build

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 12-20-2019
 */
object SysUtil {

    fun isAndroidMOrLater(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}