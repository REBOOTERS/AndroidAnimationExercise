package com.engineer.imitate.ui.widget.shimmer

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.engineer.imitate.util.dp


class ShimmerLayout @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributes, defStyleAttr) {

    private val shimmerDrawable = ShimmerDrawable()

    private val paint = Paint()

    private val path = Path()
    private val radius = 22.dp.toFloat()
    private val radii = floatArrayOf(radius, radius, radius, radius, radius, radius, 0f, 0f)

    init {
        setWillNotDraw(false)
        shimmerDrawable.callback = this
//        paint.color = Color.TRANSPARENT
        paint.isAntiAlias = true
    }

    fun startAnim() {
        shimmerDrawable.startAnim()
    }

    fun stopAnim() {
        shimmerDrawable.stopAnim()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        path.addRoundRect(rectF, radii, Path.Direction.CW)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        shimmerDrawable.setBounds(0, 0, width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.clipPath(path)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        canvas?.let {
            shimmerDrawable.draw(canvas)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnim()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnim()
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who == shimmerDrawable
    }
}

class ShimmerDrawable : Drawable() {
    private val TAG = "ShimmerDrawable"
    private val mShimmerPaint = Paint()
    private val mDrawRect = Rect()
    private val mShadowMatrix = Matrix()

    private var valueAnimator: ValueAnimator? = null

    init {
        mShimmerPaint.isAntiAlias = true
        mShimmerPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)
    }

    fun startAnim() {
        valueAnimator?.let {
            if (isAnimStarted().not() && callback != null) {
                it.start()
            }
        }
        updateShader()
        updateValueAnimator()
        invalidateSelf()
    }

    fun stopAnim() {
        valueAnimator?.let {
            if (isAnimStarted()) {
                it.cancel()
            }
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        bounds?.let {
            mDrawRect.set(it)
            updateShader()
            startAnim()
        }
    }

    override fun draw(canvas: Canvas) {
        Log.e(TAG, "draw ${mShimmerPaint.shader}")
        if (mShimmerPaint.shader == null) {
            return
        }

        val translateWidth = mDrawRect.width()
        val animateValue =
            if (valueAnimator != null) valueAnimator!!.animatedValue as Float else 0f
        val dx = offset(-translateWidth, translateWidth, animateValue)
        Log.e(TAG, "dx=$dx,rect=$mDrawRect")
        mShadowMatrix.reset()
        mShadowMatrix.setRotate(10f, mDrawRect.width() / 2f, mDrawRect.height() / 2f)
        mShadowMatrix.postTranslate(dx, 0f)
        mShimmerPaint.shader.setLocalMatrix(mShadowMatrix)
        canvas.drawRect(mDrawRect, mShimmerPaint)
    }

    private fun offset(start: Int, end: Int, percent: Float): Float {
        return start + (end - start) * percent
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    @Deprecated("don't use", replaceWith = ReplaceWith(""))
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    ///////
    private fun isAnimStarted(): Boolean {
        return valueAnimator?.isStarted ?: false
    }

    private fun updateShader() {
        val boundsW = bounds.width()
        val boundsH = bounds.height()
        if (boundsH == 0 || boundsW == 0) {
            return
        }
        val shader = LinearGradient(
            0f, 0f, boundsW.toFloat(), 0f,
            Shimmer.colors, Shimmer.position, Shader.TileMode.CLAMP
        )
        mShimmerPaint.shader = shader
    }

    private fun updateValueAnimator() {
        var started = false
        valueAnimator?.let {
            started = it.isStarted
            it.cancel()
            it.removeAllUpdateListeners()
        }
        valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            interpolator = LinearInterpolator()
            duration = Shimmer.duration
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                invalidateSelf()
            }
        }
        if (started) {
            valueAnimator?.start()
        }
    }
}

object Shimmer {
    private const val baseColor = Color.TRANSPARENT
    private const val highlightColor = 0x66ffffff
    const val duration = 1940L

    val colors = intArrayOf(baseColor, highlightColor, highlightColor, baseColor)
//    val colors = intArrayOf(baseColor, highlightColor, baseColor)

    val position = floatArrayOf(0.7f, 0.8f, 0.9f, 1.0f)
//        val position = floatArrayOf(0.8f, 0.9f, 1f)
//    val position = floatArrayOf(0.5f, 0.6f, 0.7f)
}