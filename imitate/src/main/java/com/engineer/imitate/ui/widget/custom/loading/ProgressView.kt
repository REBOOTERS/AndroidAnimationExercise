package com.engineer.imitate.ui.widget.custom.loading

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2018/1/14
 * desc   :
 * version: 1.0
 */
class ProgressView : View {

    // 最大值 一小时
    private lateinit var mCirclePaint: Paint
    private lateinit var mArcPaint: Paint
    private var mTextPaint: TextPaint? = null
    private val WHITE = Color.WHITE
    private val RED = Color.RED
    private var centerX = 0
    private var centerY = 0
    private var radius = 0
    private lateinit var mRectF: RectF

    //
    private var degree = 0f

    constructor(context: Context) : super(context) {
        initPaint(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initPaint(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initPaint(context)
    }

    private fun initPaint(context: Context) {
        mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = WHITE
            strokeWidth = 12f
            style = Paint.Style.STROKE
        }

        mArcPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = RED
            style = Paint.Style.STROKE
            strokeWidth = 12f
        }


        mTextPaint = TextPaint()
        mTextPaint!!.color = RED
        mTextPaint!!.textSize = 80f
        mTextPaint!!.textAlign = Paint.Align.CENTER
    }

    fun setProgress(percent: Float) {
        degree = percent * 360
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), mCirclePaint)
        canvas.drawArc(mRectF, -90f, degree, false, mArcPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRectF = RectF(
            (centerX - radius).toFloat(), (centerY - radius).toFloat(),
            (centerX + radius).toFloat(), (centerY + radius).toFloat()
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val min = Math.min(measuredHeight, measuredWidth)
        // 规定视图大小为和屏幕同宽的正方形
        setMeasuredDimension(min, min)
        centerX = min / 2
        centerY = min / 2
        radius = min / 3
        Log.e(TAG, "onMeasure: radius=$radius")
    }

    companion object {
        private const val TAG = "ClockView"
    }
}