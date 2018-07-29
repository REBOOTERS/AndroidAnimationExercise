package com.engineer.phenix.internal.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 *
 * @FileName:com.engineer.phenix.internal.view.PhenixViewPager.java
 * @author: rookie
 * @date: 2018-07-20 17:50
 * @version V1.0
 */

class PhenixViewPager : ViewPager {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return try {
            super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            false
        }
    }

}