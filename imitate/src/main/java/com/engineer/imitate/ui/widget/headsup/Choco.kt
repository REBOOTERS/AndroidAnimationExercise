package com.engineer.imitate.ui.widget.headsup

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.*

/**
 * @author:         https://github.com/o0o0oo00
 * @description:    custom Choco view
 *
 * @date:           2019/3/15
 */
class Choco @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var animEnter: ObjectAnimator
    private val animEnterInterceptor = OvershootInterpolator()
    private var enableIconPulse = true

    var enableInfiniteDuration = false
    private var enableProgress = false
    private var enabledVibration = false
    private var buttons = ArrayList<Button>()

    private var onShow: (() -> Unit)? = null
    private var onDismiss: (() -> Unit)? = null

    private var onlyOnce = true


    /**
     * 初始化配置，如loading 的显示 与 icon的动画 触摸反馈等
     */
    private fun initConfiguration() {

//        if (enableIconPulse) {
//            icon?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.alerter_pulse))
//        }
//
//        if (enableProgress) {
//            icon.visibility = View.GONE
//            progress.visibility = View.VISIBLE
//        } else {
//            icon.visibility = View.VISIBLE
//            progress.visibility = View.GONE
//        }
//
//        buttons.forEach {
//            buttonContainer.addView(it)
//        }

        if (enabledVibration) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.e(TAG, "onAttachedToWindow")
        initConfiguration()
        onShow?.invoke()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.e(TAG, "onDetachedFromWindow")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e(TAG, "onMeasure")

        if (onlyOnce) {
            Log.e(TAG, "height==" + this@Choco.measuredHeight)
            onlyOnce = false
            animEnter = ObjectAnimator.ofFloat(
                this@Choco,
                "translationY",
                -this@Choco.measuredHeight.toFloat(),
                -80F
            )
            animEnter.interpolator = animEnterInterceptor
            animEnter.duration = ANIMATION_DURATION
            animEnter.start()
        }
    }

    fun onShow(onShow: () -> Unit) {
        this.onShow = onShow
    }

    fun onDismiss(onDismiss: () -> Unit) {
        this.onDismiss = onDismiss
    }

    fun hide(removeNow: Boolean = false) {
        if (!this@Choco.isAttachedToWindow) {
            return
        }
        val windowManager =
            (this.context.getSystemService(Context.WINDOW_SERVICE) as? WindowManager) ?: return
        if (removeNow) {
            if (this@Choco.isAttachedToWindow) {
                onDismiss?.invoke()
                windowManager.removeViewImmediate(this@Choco)
            }
            return
        }
        isClickable = false
        val anim = ObjectAnimator.ofFloat(
            this@Choco,
            "translationY",
            -80F,
            -this@Choco.measuredHeight.toFloat()
        )
        anim.interpolator = AnticipateOvershootInterpolator()
        anim.duration = ANIMATION_DURATION
        anim.start()
        Handler().postDelayed({
            if (this@Choco.isAttachedToWindow) {
                onDismiss?.invoke()
                windowManager.removeViewImmediate(this@Choco)
            }
        }, ANIMATION_DURATION)
    }


    fun setChocoBackgroundColor(@ColorInt color: Int) {
        setBackgroundColor(color)
    }

    /**
     * Sets the Choco Background Drawable Resource
     *
     * @param resource The qualified drawable integer
     */
    fun setChocoBackgroundResource(@DrawableRes resource: Int) {
        setBackgroundResource(resource)
    }


    /**
     * Enable or Disable haptic feedback
     *
     * @param vibrationEnabled True to enable, false to disable
     */
    fun setEnabledVibration(enabledVibration: Boolean) {
        this.enabledVibration = enabledVibration
    }


    /**
     * Set whether to enable swipe to dismiss or not
     */
//    fun enableSwipeToDismiss() {
//        body.setOnTouchListener(SwipeDismissTouchListener(body, object : SwipeDismissTouchListener.DismissCallbacks {
//            override fun canDismiss(): Boolean {
//                return true
//            }
//
//            override fun onDismiss(view: View) {
//                hide(true)
//            }
//
//            override fun onTouch(view: View, touch: Boolean) {
//                // Ignore
//            }
//        }))
//    }

    companion object {
        private val TAG = Choco::class.java.simpleName
        const val DISPLAY_TIME: Long = 3000
        const val ANIMATION_DURATION: Long = 500
        private const val MUL = -0x1000000

    }

}