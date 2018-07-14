package com.engineer.imitate.widget.custom

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
import android.view.animation.BounceInterpolator


class CircleLoadingUltimate : View, Animatable, ValueAnimator.AnimatorUpdateListener {


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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)



        canvas?.save()
        canvas?.drawArc(mInnerCircleRectF1, 0.0f,  - mRotateAngle, false, mStrokePaint)
        canvas?.drawArc(mInnerCircleRectF2, 0.0f,  mRotateAngle, false, mStrokePaint)
        canvas?.drawArc(mInnerCircleRectF3, 0.0f,  - mRotateAngle, false, mStrokePaint)
        canvas?.drawArc(mInnerCircleRectF4, 0.0f,  mRotateAngle, false, mStrokePaint)
        canvas?.drawArc(mInnerCircleRectF5, 0.0f, - mRotateAngle, false, mStrokePaint)
        canvas?.drawArc(mInnerCircleRectF6, 0.0f,  mRotateAngle, false, mStrokePaint)
        canvas?.restore()
    }

    public fun startLoading() {
        start()
    }

    public fun stopLoading() {
        stop()
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        if (animation != null) {
            mRotateAngle = animation.animatedValue as Float
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
    private val ANIMATION_DURATION: Long = 1000

    private lateinit var mFloatValueAnimator: ValueAnimator
    private lateinit var mStrokePaint: Paint
    private lateinit var mInnerCircleRectF1: RectF
    private lateinit var mInnerCircleRectF2: RectF
    private lateinit var mInnerCircleRectF3: RectF
    private lateinit var mInnerCircleRectF4: RectF
    private lateinit var mInnerCircleRectF5: RectF
    private lateinit var mInnerCircleRectF6: RectF

    private var centerX: Int = 0
    private var centerY: Int = 0

    private val lineWidth = 10.0f

    private var mRotateAngle: Float = 0.toFloat()

    init {
        initAnimations()
        initPaint()
    }

    private fun initPaint() {
        mInnerCircleRectF1 = RectF()
        mInnerCircleRectF2 = RectF()
        mInnerCircleRectF3 = RectF()
        mInnerCircleRectF4 = RectF()
        mInnerCircleRectF5 = RectF()
        mInnerCircleRectF6 = RectF()

        //
        mStrokePaint = Paint()
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.strokeWidth = lineWidth
        mStrokePaint.color = Color.RED
        mStrokePaint.isAntiAlias = true
        mStrokePaint.strokeCap = Paint.Cap.ROUND
        mStrokePaint.strokeJoin = Paint.Join.ROUND


    }

    private fun initAnimations() {
        mFloatValueAnimator = ValueAnimator.ofFloat(360f)
        mFloatValueAnimator.repeatCount = Animation.INFINITE
        mFloatValueAnimator.repeatMode = ValueAnimator.REVERSE
        mFloatValueAnimator.duration = ANIMATION_DURATION
        mFloatValueAnimator.startDelay = ANIMATION_START_DELAY
        mFloatValueAnimator.interpolator = AccelerateInterpolator()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = width / 2
        centerY = height / 2

        val outR = centerX - lineWidth
        var inR1 = outR * 1f
        var inR2 = outR * 0.83f
        var inR3 = outR * 0.61f
        var inR4 = outR * 0.49f
        var inR5 = outR * 0.33f
        var inR6 = outR * 0.16f


        mInnerCircleRectF1.set(centerX - inR1, centerY - inR1, centerX + inR1, centerY + inR1)
        mInnerCircleRectF2.set(centerX - inR2, centerY - inR2, centerX + inR2, centerY + inR2)
        mInnerCircleRectF3.set(centerX - inR3, centerY - inR3, centerX + inR3, centerY + inR3)
        mInnerCircleRectF4.set(centerX - inR4, centerY - inR4, centerX + inR4, centerY + inR4)
        mInnerCircleRectF5.set(centerX - inR5, centerY - inR5, centerX + inR5, centerY + inR5)
        mInnerCircleRectF6.set(centerX - inR6, centerY - inR6, centerX + inR6, centerY + inR6)
    }
}