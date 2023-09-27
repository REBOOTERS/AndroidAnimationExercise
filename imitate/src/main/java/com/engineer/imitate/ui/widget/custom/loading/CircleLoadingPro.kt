package com.engineer.imitate.ui.widget.custom.loading

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation


class CircleLoadingPro: View, Animatable, ValueAnimator.AnimatorUpdateListener {


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startLoading()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopLoading()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        centerX = width / 2
        centerY = height / 2

        val outR = centerX - lineWidth
        var inR = outR * 0.75f
        var innR = outR *0.5f
        var minR = outR *0.25f

        mOuterCircleRectF.set(centerX - outR, centerY - outR, centerX + outR, centerY + outR)
        mInnerCircleRectF.set(centerX - inR, centerY - inR, centerX + inR, centerY + inR)
        mInnerMoreCircleRectF.set(centerX - innR, centerY - innR, centerX + innR, centerY + innR)
        mMinCircleRectF.set(centerX - minR, centerY - minR, centerX + minR, centerY + minR)

        canvas?.save()
        canvas?.drawArc(mOuterCircleRectF, mRotateAngle % 360, OUTER_CIRCLE_ANGLE, false, mStrokePaint)
        canvas?.drawArc(mInnerCircleRectF, 270 - mRotateAngle % 360, INTER_CIRCLE_ANGLE, false, mStrokePaint)
        canvas?.drawArc(mInnerMoreCircleRectF,   mRotateAngle % 360, OUTER_CIRCLE_ANGLE, false, mStrokePaint)
        canvas?.drawArc(mMinCircleRectF,   270-mRotateAngle % 360, INTER_CIRCLE_ANGLE, false, mStrokePaint)
        canvas?.restore()
    }

    public fun startLoading() {
        start()
    }

    public fun stopLoading() {
        stop()
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        if (animation != null) {
            mRotateAngle = 360.0f * animation.animatedValue as Float
            invalidate()
        }
    }

    override fun isRunning(): Boolean {
        return mFloatValueAnimator.isRunning
    }

    override fun start() {
        if (mFloatValueAnimator.isStarted) {
            return
        }

        mFloatValueAnimator.addUpdateListener(this)
        mFloatValueAnimator.start()
    }

    override fun stop() {
        mFloatValueAnimator.removeAllUpdateListeners()
        mFloatValueAnimator.end()
    }


    private val ANIMATION_START_DELAY: Long = 200
    private val ANIMATION_DURATION: Long = 800
    private val OUTER_CIRCLE_ANGLE = 270.0f
    private val INTER_CIRCLE_ANGLE = 90.0f

    private lateinit var mFloatValueAnimator: ValueAnimator
    private lateinit var mStrokePaint: Paint
    private lateinit var mOuterCircleRectF: RectF
    private lateinit var mInnerCircleRectF: RectF
    private lateinit var mInnerMoreCircleRectF:RectF
    private lateinit var mMinCircleRectF:RectF

    private var centerX: Int = 0
    private var centerY: Int = 0

    private val lineWidth = 10.0f

    private var mRotateAngle: Float = 0.toFloat()

    init {
        initAnimations()
        initPaint()
    }

    private fun initPaint() {
        mOuterCircleRectF = RectF()
        mInnerCircleRectF = RectF()
        mInnerMoreCircleRectF = RectF()
        mMinCircleRectF = RectF()

        //
        mStrokePaint = Paint()
        mStrokePaint.style=Paint.Style.STROKE
        mStrokePaint.strokeWidth = lineWidth
        mStrokePaint.color = Color.RED
        mStrokePaint.isAntiAlias = true
        mStrokePaint.strokeCap = Paint.Cap.ROUND
        mStrokePaint.strokeJoin = Paint.Join.ROUND


    }

    private fun initAnimations() {
        mFloatValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
        mFloatValueAnimator.repeatCount = Animation.INFINITE
        mFloatValueAnimator.duration = ANIMATION_DURATION
        mFloatValueAnimator.startDelay = ANIMATION_START_DELAY
        mFloatValueAnimator.interpolator = AccelerateInterpolator()
    }


}