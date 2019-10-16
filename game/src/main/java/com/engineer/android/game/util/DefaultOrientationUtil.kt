package com.engineer.android.game.util

import android.util.DisplayMetrics
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 10-15-2019
 */
object DefaultOrientationUtil {


    @JvmStatic
    fun getOrientation(activity: AppCompatActivity):DefaultOrientation {

        val display = activity.windowManager.defaultDisplay
        val rotation = display.rotation
        var widthOrigin = 0
        var heightOrigin = 0

        val dm = DisplayMetrics()
        display.getMetrics(dm)
        when (rotation) {
            Surface.ROTATION_0,Surface.ROTATION_180 -> {
                widthOrigin = dm.widthPixels
                heightOrigin = dm.heightPixels
            }
            Surface.ROTATION_90,Surface.ROTATION_270 -> {
                widthOrigin = dm.heightPixels
                heightOrigin = dm.widthPixels
            }
        }
        return if (widthOrigin > heightOrigin) {
            DefaultOrientation.LANDSCAPE
        } else {
            DefaultOrientation.PORTRAIT
        }
    }
}