package com.engineer.imitate.ui.widget.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout

/**
 * Created on 2019/3/7.
 * @author rookie
 */

const val TAG = "StackViewLayout"

class StackViewLayout : LinearLayout {


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        orientation = HORIZONTAL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e(TAG,"width==${MeasureSpec.getSize(widthMeasureSpec)}")
        if (childCount == 0) {
            return
        }
        val child = getChildAt(0)
        val childW = child.measuredWidth
        val childH = child.measuredHeight
        val width = childW * childCount - childW / 4 * (childCount - 1)
        val s = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST)
        val t = MeasureSpec.makeMeasureSpec(childH, MeasureSpec.EXACTLY)
        setMeasuredDimension(s, t)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        var step = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)

            if (child.visibility == View.VISIBLE) {
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                val childTop = child.top
                var start = 0

                Log.e(TAG, "i==$i, layoutDirection==${parent.layoutDirection}")



                if (this.layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                    start = child.left + (childWidth / 4) * step
                } else if (this.layoutDirection == View.LAYOUT_DIRECTION_LTR) {
                    start = child.left - (childWidth / 4) * step
                }

                child.layout(start, childTop, start + childWidth, childTop + childHeight)
                step++

            }

        }

    }
}