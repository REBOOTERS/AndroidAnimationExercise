package com.engineer.imitate.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.engineer.imitate.R
import java.util.concurrent.TimeUnit

/**
 *
 * @author: zhuyongging
 * @since: 2019-04-12
 */

fun View.setBorderlessBackground() {
    val attrs = intArrayOf(R.attr.selectableItemBackgroundBorderless)
    val typeArray = context.obtainStyledAttributes(attrs)
    val backgroundRes = typeArray.getResourceId(0, 0)
    setBackgroundResource(backgroundRes)
    typeArray.recycle()

}


fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun View.clicks(skipDuration:Long = 500, block: (View)-> Unit) {
    val throttle = Throttle(skipDuration, TimeUnit.MILLISECONDS)
    setOnClickListener {
        if (throttle.needSkip()) return@setOnClickListener
        block(it)
    }
}

fun View.visible(value: Boolean) {
    if (value) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}