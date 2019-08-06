package com.engineer.imitate.ui.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.math.MathUtils
import androidx.core.view.ScrollingView
import com.engineer.imitate.R

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 08-06-2019
 */
class ParallaxView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private val scrollListener = ViewTreeObserver.OnScrollChangedListener {
        updateTransformation()
    }

    private val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            initScrollableParent()
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        initScrollableParent()

        if (isNeedScale) {
            scaleX = parallaxScale
            scaleY = parallaxScale
        } else {
            scaleX = 1.0f
            scaleY = 1.0f
        }

        /*
            This crutch is extremely needed,
            because right coordinates are available on 5-6 call of getLocationInWindow()
         */
        for (i in 1..5) postDelayed({ updateTransformation() }, 50)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        viewTreeObserver.addOnScrollChangedListener(scrollListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnScrollChangedListener(scrollListener)
    }

    private var scrollableParent: View? = null
    private val scrollableParentLocation = IntArray(2)
    private var location = IntArray(2)

    var isEnabledHorizontalParallax = true
    var isEnabledVerticalParallax = true
    var isInvertedHorizontalParallax = false
    var isInvertedVerticalParallax = false
    var isNeedScale = false
    var parallaxScale = 1.5f
    var decelerateFactor = 0.2f

    private fun initScrollableParent() {
        if (scrollableParentLocation[0] != 0 && scrollableParentLocation[1] != 0) return
        var viewParent = parent
        while (viewParent is View) {
            if (viewParent is ScrollingView ||
                viewParent is ScrollView ||
                viewParent is HorizontalScrollView ||
                viewParent is AdapterView<*>
            ) {
                scrollableParent = viewParent
                viewParent.getLocationInWindow(scrollableParentLocation)
                break
            }
            viewParent = viewParent.getParent()
        }
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        clipChildren = true

        val a = context?.obtainStyledAttributes(attrs, R.styleable.ParallaxView)
        isEnabledHorizontalParallax =
            a?.getBoolean(R.styleable.ParallaxView_isEnabledHorizontalParallax, isEnabledHorizontalParallax)
                ?: isEnabledHorizontalParallax
        isEnabledVerticalParallax =
            a?.getBoolean(R.styleable.ParallaxView_isEnabledVerticalParallax, isEnabledVerticalParallax)
                ?: isEnabledVerticalParallax
        isInvertedHorizontalParallax =
            a?.getBoolean(R.styleable.ParallaxView_isInvertedHorizontalParallax, isInvertedHorizontalParallax)
                ?: isInvertedHorizontalParallax
        isInvertedVerticalParallax =
            a?.getBoolean(R.styleable.ParallaxView_isInvertedVerticalParallax, isInvertedVerticalParallax)
                ?: isInvertedVerticalParallax
        isNeedScale = a?.getBoolean(R.styleable.ParallaxView_isNeedScale, isNeedScale) ?: isNeedScale
        decelerateFactor = a?.getFloat(R.styleable.ParallaxView_decelerateFactor, decelerateFactor) ?: decelerateFactor
        parallaxScale = a?.getFloat(R.styleable.ParallaxView_parallaxScale, parallaxScale) ?: parallaxScale
        MathUtils.clamp(decelerateFactor, 0f, 1f)
        a?.recycle()
    }

    private fun updateTransformation() {
        if (scrollableParent == null) return

        getLocationInWindow(location)

        var viewCenter: Int
        if (isEnabledHorizontalParallax) {
            viewCenter = location[0] + (width * scaleX / 2).toInt()
            translationX =
                (viewCenter - (scrollableParentLocation[0] + scrollableParent!!.width / 2)) * decelerateFactor * if (isInvertedHorizontalParallax) -1 else 1
        }

        if (isEnabledVerticalParallax) {
            viewCenter = location[1] + (height * scaleY / 2).toInt()
            translationY =
                (viewCenter - (scrollableParentLocation[1] + scrollableParent!!.height / 2)) * decelerateFactor * if (isInvertedVerticalParallax) -1 else 1
        }
    }

    private fun isVisible(): Boolean {
        if (!isShown) return false

        val actualPosition = Rect()
        getGlobalVisibleRect(actualPosition)
        val screenWidth = context.resources.displayMetrics.widthPixels
        val screenHeight = context.resources.displayMetrics.heightPixels
        val screen = Rect(0, 0, screenWidth, screenHeight)
        return actualPosition.intersect(screen)
    }
}