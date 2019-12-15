package home.smart.fly.animations.utils

import android.os.Build

/**
 * @author rookie
 * @since 12-14-2019
 */
object SysUtil {

    @JvmStatic
    fun Android8OrLater(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O
    }
}