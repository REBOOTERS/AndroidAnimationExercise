package home.smart.fly.animations.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @author rookie
 * @since 12-14-2019
 */
object SysUtil {

    @JvmStatic
    fun Android8OrLater(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O
    }

    @JvmStatic
    fun timeStampToDate(timeInSeconds: String): String {
        return timeStampToDate(timeInSeconds.toLong() * 100)
    }

    @JvmStatic
    fun timeStampToDate(timeInMillionSeconds: Long): String {
        val format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(timeInMillionSeconds))
    }
}