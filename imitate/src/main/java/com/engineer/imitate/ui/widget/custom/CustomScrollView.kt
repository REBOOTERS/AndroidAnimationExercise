package com.engineer.imitate.ui.widget.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller

/**
 *
 * @author: Rookie
 * @date: 2018-10-30
 * @desc
 */
class CustomScrollView : View {

    private val TAG = "CustomScrollView"
    private lateinit var mScroller: Scroller

    private var mLastX = 0
    private var mLastY = 0

    private lateinit var mPaint: Paint
    private lateinit var mTextPaint: TextPaint
    private var mRadius = 0.0f
    private var mCenterX = 0.0f
    private var mCenterY = 0.0f

    private lateinit var mView: View


    //<editor-fold desc="init">
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }
    //</editor-fold>

    fun init(context: Context) {
        mScroller = Scroller(context)

        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = Color.RED

        mTextPaint = TextPaint()
        mTextPaint.color = Color.WHITE
        mTextPaint.textSize = 42.0f
        mTextPaint.textAlign = Paint.Align.CENTER
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        Log.e(TAG, "onSizeChanged")
        super.onSizeChanged(w, h, oldw, oldh)
        mView = parent as View
        mCenterX = (measuredWidth / 2).toFloat()
        mCenterY = (measuredHeight / 2).toFloat()
        mRadius = Math.min(measuredHeight / 2, measuredWidth / 2).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.e(TAG, "onMeasure")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = resources.displayMetrics.widthPixels
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = resources.displayMetrics.widthPixels
        }
        setMeasuredDimension(widthSize, heightSize)


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(mCenterX, mCenterY, mRadius / 2, mPaint)
        canvas.drawText("Drag Me", mCenterX, mCenterY, mTextPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x: Int = event.x.toInt()
        val y: Int = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = x
                mLastY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - mLastX
                val deltaY = y - mLastY
                mView.scrollBy(-deltaX, -deltaY)
            }
            MotionEvent.ACTION_UP -> {
//                mView.scrollTo(0, 0)
            }
        }
        return super.onTouchEvent(event)
    }


    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            mView.scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }

    fun smoothScrollTo(destX: Int, destY: Int) {
        val deltaX = destX - scrollX
        val deltaY = destY - scrollY
        mScroller.startScroll(scrollX, scrollY, deltaX, deltaY)
        invalidate()
    }
}