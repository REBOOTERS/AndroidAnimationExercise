package com.engineer.imitate.widget.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Scroller

/**
 *
 * @author: zhuyongging
 * @date: 2018-10-30
 * @desc
 */
class CustomScrollView: View {

    private lateinit var mScroller: Scroller

    //<editor-fold desc="init">
    constructor(context:Context) :super(context) {init(context)}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }
    //</editor-fold>

    fun init(context: Context){
        mScroller=Scroller(context)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            val view = parent as View
            view.scrollTo(mScroller.currX,mScroller.currY)
            invalidate()
        }
    }

    public fun smoothScrollTo(destX: Int, destY: Int) {
        val deltaX = destX - scrollX
        val deltaY = destY -scrollY
        mScroller.startScroll(scrollX,scrollY,deltaX,deltaY,2000)
        invalidate()
    }
}