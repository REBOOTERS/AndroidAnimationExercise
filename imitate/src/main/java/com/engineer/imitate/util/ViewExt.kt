package com.engineer.imitate.util

import android.view.View
import com.engineer.imitate.R

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