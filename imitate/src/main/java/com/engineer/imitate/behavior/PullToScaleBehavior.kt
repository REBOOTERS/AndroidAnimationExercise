package com.engineer.imitate.behavior

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import com.google.android.material.appbar.AppBarLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import com.engineer.imitate.behavior.internel.DisInterceptNestedScrollView

/**
 *
 * @author: zhuyongging
 * @since: 2019-01-15
 */
class PullToScaleBehavior : AppBarLayout.Behavior {

    constructor(context: Context) : super()

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    //
    private lateinit var mTargetView: View

    private val TARGET_TAG = "scale"
    private val TARGET_HEIGHT = 1500


    private var mParentHeight = 0
    private var mTargetViewHeight = 0
    private var isAnimation = false
    private var isRecovering = false

    private var mTotalDy = 0
    private var mLastScale = 0f
    private var mLastBottom = 0

    override fun onLayoutChild(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, abl: AppBarLayout, layoutDirection: Int): Boolean {

        mTargetView = parent.findViewWithTag(TARGET_TAG)
        initAppBarLayout(abl)
        return super.onLayoutChild(parent, abl, layoutDirection)
    }

    private fun initAppBarLayout(abl: AppBarLayout) {
        abl.clipChildren = false
        mParentHeight = abl.height
        mTargetViewHeight = mTargetView.height
    }

    override fun onStartNestedScroll(parent: androidx.coordinatorlayout.widget.CoordinatorLayout, child: AppBarLayout, directTargetChild: View, target: View, nestedScrollAxes: Int, type: Int): Boolean {
        isAnimation = true
        if (target is DisInterceptNestedScrollView) {
            return true
        }
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
    }

    override fun onNestedPreScroll(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout, child: AppBarLayout, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (!isRecovering) {
            val canDown = dy > 0 && child.bottom > mParentHeight
            val canUp = dy < 0 && child.bottom >= mParentHeight

            if (canDown || canUp) {
                scale(child, target, dy)
                return
            }
        }
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    private fun scale(child: AppBarLayout, target: View, dy: Int) {
        mTotalDy -= dy
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT)
        mLastScale = Math.max(1f, 1.0f + mTotalDy / TARGET_HEIGHT)
        ViewCompat.setScaleX(mTargetView, mLastScale)
        ViewCompat.setScaleY(mTargetView, mLastScale)
        mLastBottom = mParentHeight + (mTargetViewHeight / 2 * (mLastScale - 1)).toInt()
        child.bottom = mLastBottom
        target.scrollY = 0
    }

    override fun onNestedPreFling(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout, child: AppBarLayout, target: View, velocityX: Float, velocityY: Float): Boolean {
        if (velocityY > 100) {
            isAnimation = false
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }


    override fun onStopNestedScroll(coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout, abl: AppBarLayout, target: View, type: Int) {
        recovery(abl)
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
    }

    private fun recovery(abl: AppBarLayout) {
        if (isRecovering) return
        if (mTotalDy > 0) {
            isRecovering = true
            mTotalDy = 0
            if (isAnimation) {
                val anim = ValueAnimator.ofFloat(mLastScale, 1f).setDuration(200)
                anim.addUpdateListener { animation ->
                    val value = animation.animatedValue as Float
                    ViewCompat.setScaleX(mTargetView, value)
                    ViewCompat.setScaleY(mTargetView, value)
                    abl.bottom = (mLastBottom - (mLastBottom - mParentHeight) * animation.animatedFraction).toInt()


                }
                anim.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        isRecovering = false
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
                anim.start()
            } else {
                ViewCompat.setScaleX(mTargetView, 1f)
                ViewCompat.setScaleY(mTargetView, 1f)
                abl.bottom = mParentHeight

                isRecovering = false

            }
        }
    }

}