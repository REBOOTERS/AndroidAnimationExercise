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
import com.engineer.imitate.util.dp2px

/**
 * @author rookie
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
    private var mRadius = 5f
    private var mColor = Color.BLUE
    private val maxCount = 3
    private var count = 0
    private lateinit var rect: RectF
    private var OneDp = 0f
    private var mContext: Context? = null


    private fun initView(context: Context?) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style = Paint.Style.STROKE
        mPaint.color = mColor
        mPaint.alpha = 0
        mPaint.strokeWidth = mStrokeWidth
        setWillNotDraw(false)
        mContext = context
        OneDp = context?.dp2px(1f) ?: 0f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        Log.e("zyq", "w1==: $w")
        Log.e("zyq", "h1==: $h")
        Log.e("zyq", "5dp==: $OneDp")
        rect = RectF(
            OneDp * 5 + mStrokeWidth / 2f, OneDp * 5 + mStrokeWidth / 2f,
            width - OneDp * 5 - mStrokeWidth / 2f, height - OneDp * 5 - mStrokeWidth / 2f
        )
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(rect, mRadius, mRadius, mPaint)
        super.onDraw(canvas)
    }

    fun startAnim() {
        count = 0
        val radiusAnim = ValueAnimator.ofFloat(5f, 6f, 7f)
        radiusAnim.addUpdateListener {
            val value: Float = it.animatedValue as Float
            mRadius = value
            val real = 5 * (1 - it.animatedFraction)
            rect = RectF(
                OneDp * real + mStrokeWidth / 2f, OneDp * real + mStrokeWidth / 2f,
                width - OneDp * real - mStrokeWidth / 2f, height - OneDp * real - mStrokeWidth / 2f
            )
            invalidate()
        }
        radiusAnim.duration = 400
        radiusAnim.repeatMode = ValueAnimator.RESTART
        radiusAnim.start()

        val alpha = ValueAnimator.ofInt(0, 255, 0)
        alpha.addUpdateListener {
            val value: Int = it.animatedValue as Int
            mPaint.alpha = value
        }
        alpha.duration = 400
        alpha.repeatMode = ValueAnimator.RESTART

        val set = AnimatorSet()

        set.playTogether(radiusAnim, alpha)
        set.duration = 800
        set.start()

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                count += 1
                if (count < maxCount) {
                    set.startDelay = 400
                    set.start()
                }
            }
        })
    }
}