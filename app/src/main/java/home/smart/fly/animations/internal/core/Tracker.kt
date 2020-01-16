package home.smart.fly.animations.internal.core

import android.view.View

/**
 * @author rookie
 * @since 01-08-2020
 */
object Tracker {

    @JvmStatic
    fun c(v: View) {
        BeautyLog.printClickInfo(v)
    }

    @JvmStatic
    fun c(v: View, className: String) {
        BeautyLog.printClickInfo(className, v)
    }
}