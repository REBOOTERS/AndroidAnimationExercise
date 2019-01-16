package com.engineer.imitate.behavior.internel

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 *
 * @author: zhuyongging
 * @since: 2019-01-15
 */
class DisInterceptNestedScrollView : NestedScrollView {

    constructor(context: Context) : super(context) {
        requestDisallowInterceptTouchEvent(true)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        requestDisallowInterceptTouchEvent(true)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        requestDisallowInterceptTouchEvent(true)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_MOVE -> requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_UP -> requestDisallowInterceptTouchEvent(false)
            MotionEvent.ACTION_CANCEL -> requestDisallowInterceptTouchEvent(false)
        }
        return super.onTouchEvent(ev)
    }
}