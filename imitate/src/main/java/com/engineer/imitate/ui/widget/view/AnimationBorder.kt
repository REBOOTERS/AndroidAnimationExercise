package com.engineer.imitate.ui.widget.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
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
    private val maxCount = 3
    private var count = 0
    private lateinit var rect: RectF


    private fun initView(context: Context?) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mColor
        mPaint.alpha = 1
        mPaint.strokeWidth = mStrokeWidth
        setWillNotDraw(false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect = RectF(
            mStrokeWidth / 2f,
            mStrokeWidth / 2f,
            width - mStrokeWidth / 2f,
            height - mStrokeWidth / 2f
        )
    }

    override fun onDraw(canvas: Canvas?) {
        val radius = 5f
        canvas?.drawRoundRect(rect, radius, radius, mPaint)
        super.onDraw(canvas)
    }

    fun startAnim() {
        count = 0
        val stroke = ValueAnimator.ofFloat(5f, 6f, 7f)
        stroke.addUpdateListener {
            val value: Float = it.animatedValue as Float
            mPaint.strokeWidth = value
            invalidate()
        }
        stroke.duration = 400
        stroke.start()

        val alpha = ValueAnimator.ofInt(0, 255, 0)
        alpha.addUpdateListener {
            val value: Int = it.animatedValue as Int
            mPaint.alpha = value
        }
        alpha.duration = 400

        val set = AnimatorSet()

        set.playTogether(stroke, alpha)
        set.duration = 800
        set.start()

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                count += 1
                if (count < maxCount) {
                    set.start()
                }
            }
        })
    }
}