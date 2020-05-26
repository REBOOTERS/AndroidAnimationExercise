package home.smart.fly.animations.utils

import android.content.res.Resources
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * @author rookie
 * @since 05-26-2020
 */
val tag = "rookie"
fun AppCompatActivity.lg(msg: String) {
    lg(tag, msg)
}

fun AppCompatActivity.lg(tag: String, msg: String) {
    Log.e(tag, "msg is $msg")
}

val Number.dp get() = (toInt() * Resources.getSystem().displayMetrics.density).toInt()

val screenWidth = Resources.getSystem().displayMetrics.widthPixels