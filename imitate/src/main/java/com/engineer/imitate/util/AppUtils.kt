package com.engineer.imitate.util

import android.content.Context

/**
 * @author rookie
 * @since 07-30-2019
 */
class AppUtils {

    companion object {

        fun getAppVersion(context: Context): String {
            var versionStr = ""
            try {
                val manager = context.packageManager
                val info = manager.getPackageInfo(context.packageName, 0)
                versionStr = info.versionName
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return versionStr
        }


        fun getAppName(context: Context) = context.packageName
    }

}