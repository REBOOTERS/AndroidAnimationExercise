package com.engineer.imitate.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast

/**
 *
 * @author: Rookie
 * @date: 2018-08-21 09:54
 * @version V1.0
 */

fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


fun Context.dp2px(dp: Float): Float {
    val scale = resources.displayMetrics.density
    return dp * scale
}

fun Context.px2dp(px: Float): Float {
    val scale = resources.displayMetrics.density
    return px / scale
}

fun Context.getActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

