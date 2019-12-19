package com.engineer.imitate.ui.widget.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 12-19-2019
 */
class AnimationBorder : RelativeLayout {

    // <editor-fold defaultstate="collapsed" desc="constructor">
    constructor(context: Context?) : super(context) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }
    // </editor-fold>

    private lateinit var mPaint: Paint
    private var mStrokeWidth = 5f
    private var mColor = Color.RED


    private fun initView(context: Context?) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mColor
        mPaint.alpha = 0
        mPaint.strokeWidth = mStrokeWidth
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        Log.e("zyq", "x: " + x);
        Log.e("zyq", "y: " + y);
        Log.e("zyq", "left: " + left);
        Log.e("zyq", "top: " + top);
        Log.e("zyq", "right: " + right);
        Log.e("zyq", "bottom: " + bottom);
        Log.e("zyq", "width: " + width);
        Log.e("zyq", "height: " + height);
        val rect = RectF(
            mStrokeWidth / 2f,
            mStrokeWidth / 2f,
            width - mStrokeWidth / 2f,
            height - mStrokeWidth / 2f
        )
        val radius = 5f
        canvas?.drawRoundRect(rect, radius, radius, mPaint)
        super.onDraw(canvas)
    }

    fun startAnim() {
        val stroke = ValueAnimator.ofFloat(5f, 6f, 7f)
        stroke.addUpdateListener {
            val value: Float = it.animatedValue as Float
            mPaint.strokeWidth = value
            invalidate()
        }
        stroke.repeatCount = 3
        stroke.duration = 400
        stroke.start()

        val alpha = ValueAnimator.ofInt(0, 255, 0)
        alpha.addUpdateListener {
            val value: Int = it.animatedValue as Int
            mPaint.alpha = value
        }
        alpha.repeatCount = 3
        alpha.duration = 400
        alpha.start()

//        val set = AnimatorSet()
//
//        set.playTogether(stroke, alpha)
//        set.duration = 400
//        set.start()
    }
}