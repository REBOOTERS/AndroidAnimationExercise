package com.engineer.imitate.ui.widget


import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.engineer.imitate.R

open class CustomShadowLayout : FrameLayout {

    var mShadowColor: Int = 0
    var mShadowLimit: Float = 0.toFloat()
    var mCornerRadius: Float = 0.toFloat()
    val deltaFY = 1.0f
    val deltaFX = 1.0f
    var leftShow: Boolean = false
    var rightShow: Boolean = false
    var topShow: Boolean = false
    var bottomShow: Boolean = false

    private var mForceInvalidateShadow = false

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    override fun getSuggestedMinimumWidth(): Int {
        return 0
    }

    override fun getSuggestedMinimumHeight(): Int {
        return 0
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0 && (background == null || mForceInvalidateShadow)) {
            mForceInvalidateShadow = false
            setBackgroundCompat(w, h)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (mForceInvalidateShadow) {
            mForceInvalidateShadow = false
            setBackgroundCompat(right - left, bottom - top)
        }
    }

    fun invalidateShadow() {
        mForceInvalidateShadow = true
        requestLayout()
        invalidate()
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        initAttributes(context, attrs)
        notifyPadding()
    }

    private fun notifyPadding() {
        val xPadding = mShadowLimit.toInt()
        val yPadding = mShadowLimit.toInt()
        val left = if (leftShow) xPadding else 0
        val top = if (topShow) yPadding else 0
        val right = if (rightShow) xPadding else 0
        val bottom = if (bottomShow) yPadding else 0
        setPadding(left, top, right, bottom)
    }

    private fun setBackgroundCompat(w: Int, h: Int) {
        var w = w
        var h = h
        w *= deltaFX.toInt()
        h *= deltaFY.toInt()
        if (w > 0 && h > 0) {
            val bitmap = createShadowBitmap(
                w,
                h,
                mCornerRadius,
                mShadowLimit,
                mShadowColor,
                Color.TRANSPARENT
            )
            val drawable = BitmapDrawable(resources, bitmap)
            background = drawable
        }
    }


    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val attr = getTypedArray(context, attrs, R.styleable.CustomShadowLayout) ?: return

        try {
            //默认是显示
            leftShow = attr.getBoolean(R.styleable.CustomShadowLayout_panel_leftShow, true)
            rightShow = attr.getBoolean(R.styleable.CustomShadowLayout_panel_rightShow, true)
            bottomShow = attr.getBoolean(R.styleable.CustomShadowLayout_panel_bottomShow, true)
            topShow = attr.getBoolean(R.styleable.CustomShadowLayout_panel_topShow, true)

            mCornerRadius = attr.getDimension(R.styleable.CustomShadowLayout_panel_cornerRadius, 5f)
            mShadowLimit = attr.getDimension(R.styleable.CustomShadowLayout_panel_shadowLimit, 36f)
            mShadowColor =
                attr.getColor(R.styleable.CustomShadowLayout_panel_shadowColor, 0x2a000000)
        } finally {
            attr.recycle()
        }
    }

    private fun getTypedArray(
        context: Context,
        attributeSet: AttributeSet?,
        attr: IntArray
    ): TypedArray? {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
    }

    // 主要作用：获取圆形的 bitmap
    private fun createShadowBitmap(
        shadowWidth: Int, shadowHeight: Int, cornerRadius: Float, shadowRadius: Float,
        shadowColor: Int, fillColor: Int
    ): Bitmap {

        val output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val shadowRect = RectF(
            shadowRadius,
            shadowRadius,
            shadowWidth - shadowRadius,
            shadowHeight - shadowRadius
        )

        val shadowPaint = Paint()
        shadowPaint.isAntiAlias = true
        shadowPaint.color = fillColor
        shadowPaint.style = Paint.Style.FILL

        if (!isInEditMode) {
            shadowPaint.setShadowLayer(shadowRadius, 0f, 0f, shadowColor)
        }

        canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint)

        return output
    }

    fun setShadowColor(mShadowColor: Int) {
        this.mShadowColor = mShadowColor
    }

    fun setShadowLimit(mShadowLimit: Float) {
        this.mShadowLimit = mShadowLimit
        notifyPadding()
    }

    fun setCornerRadius(mCornerRadius: Float) {
        this.mCornerRadius = mCornerRadius
    }
}