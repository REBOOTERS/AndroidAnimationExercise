package com.engineer.imitate.ui.widget.more

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.core.view.NestedScrollingParent
import androidx.core.view.NestedScrollingParentHelper
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

class DZStickyNavLayouts(
    context: Context,
    attrs: AttributeSet?
) : LinearLayout(context, attrs), NestedScrollingParent {
    private val mParentHelper: NestedScrollingParentHelper
    private val mHeaderView: View
    private val mFooterView: AnimatorView
    private var mChildView: RecyclerView? = null

    // 解决多点触控问题
    private var isRunAnim = false

    interface OnStartActivityListener {
        fun onStart()
    }

    private var mLinster: OnStartActivityListener? = null
    fun setOnStartActivity(l: OnStartActivityListener?) {
        mLinster = l
    }

    override fun onFinishInflate() {
        Log.d(TAG, "onFinishInflate() called");
        super.onFinishInflate()
        orientation = HORIZONTAL
        Log.d(TAG, ": $childCount")
        Log.d(TAG, ": $maxWidth")
        if (getChildAt(0) is RecyclerView) {
            mChildView = getChildAt(0) as RecyclerView
            val layoutParams = LayoutParams(
                maxWidth,
                LayoutParams.MATCH_PARENT
            )
            addView(mHeaderView, 0, layoutParams)
            addView(mFooterView, childCount, layoutParams)
            // 左移
            scrollBy(maxWidth, 0)
            mChildView!!.setOnTouchListener { v, event -> // 保证动画状态中 子view不能滑动
                isRunAnim
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        Log.d(TAG, "onMeasure() called")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mChildView != null) {
            val params = mChildView!!.layoutParams
            params.width = measuredWidth
        }
    }


    /**
     * @param dx       水平滑动距离
     * @param dy       垂直滑动距离
     * @param consumed 父类消耗掉的距离
     */
    override fun onNestedPreScroll(
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray
    ) {
        parent.requestDisallowInterceptTouchEvent(true)
        Log.d(TAG, "onNestedPreScroll() called scrollX = " + scrollX);
        // dx>0 往左滑动 dx<0往右滑动
        //System.out.println("dx:" + dx + "=======getScrollX:" + getScrollX() + "==========canScrollHorizontally:" + !ViewCompat.canScrollHorizontally(target, -1));
        val hiddenLeft =
            dx > 0 && scrollX < maxWidth && !ViewCompat.canScrollHorizontally(
                target,
                -1
            )
        val showLeft =
            dx < 0 && !ViewCompat.canScrollHorizontally(target, -1)
        val hiddenRight =
            dx < 0 && scrollX > maxWidth && !ViewCompat.canScrollHorizontally(
                target,
                1
            )
        val showRight =
            dx > 0 && !ViewCompat.canScrollHorizontally(target, 1)
        if (hiddenLeft || showLeft || hiddenRight || showRight) {
            scrollBy(dx / DRAG, 0)
            consumed[0] = dx
        }
        if (hiddenRight || showRight) {
            mFooterView.setRefresh(dx / DRAG)
        }

        // 限制错位问题
        if (dx > 0 && scrollX > maxWidth && !ViewCompat.canScrollHorizontally(
                target,
                -1
            )
        ) {
            scrollTo(maxWidth, 0)
        }
        if (dx < 0 && scrollX < maxWidth && !ViewCompat.canScrollHorizontally(
                target,
                1
            )
        ) {
            scrollTo(maxWidth, 0)
        }
    }

    /**
     * 必须要复写 onStartNestedScroll后调用
     */
    override fun onNestedScrollAccepted(
        child: View,
        target: View,
        axes: Int
    ) {
        Log.d(TAG, "onNestedScrollAccepted() called scrollX = $scrollX")
        mParentHelper.onNestedScrollAccepted(child, target, axes)
    }

    /**
     * 返回true代表处理本次事件
     * 在执行动画时间里不能处理本次事件
     */
    override fun onStartNestedScroll(
        child: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        Log.d(TAG, "onStartNestedScroll() called scrollX = $scrollX")
        return target is RecyclerView && !isRunAnim
    }

    /**
     * 复位初始位置
     * scrollTo 移动到指定坐标
     * scrollBy 在原有坐标上面移动
     */
    override fun onStopNestedScroll(target: View) {
        mParentHelper.onStopNestedScroll(target)
        Log.d(TAG, "onStopNestedScroll() called scrollX = " + scrollX)
        // 如果不在RecyclerView滑动范围内
        if (maxWidth != scrollX) {
            startAnimation(ProgressAnimation())
        }
        if (scrollX > maxWidth + maxWidth / 2 && mLinster != null) {
            mLinster!!.onStart()
        }
    }

    /**
     * 回弹动画
     */
    private inner class ProgressAnimation : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation
        ) {
            scrollBy(
                ((maxWidth - scrollX) * interpolatedTime).toInt(),
                0
            )
            if (interpolatedTime == 1f) {
                isRunAnim = false
                mFooterView.setRelease()
            }
        }

        override fun initialize(
            width: Int,
            height: Int,
            parentWidth: Int,
            parentHeight: Int
        ) {
            super.initialize(width, height, parentWidth, parentHeight)
            duration = 300
            interpolator = AccelerateInterpolator()
        }

        init {
            isRunAnim = true
        }
    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
    }


    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
        return false
    }

    /**
     * 子view是否可以有惯性 解决右滑时快速左滑显示错位问题
     *
     * @return true不可以  false可以
     */
    override fun onNestedPreFling(
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        // 当RecyclerView在界面之内交给它自己惯性滑动
        return scrollX != maxWidth
    }

    override fun getNestedScrollAxes(): Int {
        return 0
    }

    /**
     * 限制滑动 移动x轴不能超出最大范围
     */
    override fun scrollTo(x: Int, y: Int) {
        var x = x
        if (x < 0) {
            x = 0
        } else if (x > maxWidth * 2) {
            x = maxWidth * 2
        }
        super.scrollTo(x, y)
    }

    private fun dp2Px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    companion object {
        @JvmField
        var maxWidth = 0
        private const val DRAG = 2
        val TAG = "tag"
    }

    init {
        mHeaderView = View(context)
        mHeaderView.setBackgroundColor(-0x1)
        mFooterView = AnimatorView(context)
        mFooterView.setBackgroundColor(-0x1)
        maxWidth = dp2Px(context, 120f)
        mParentHelper = NestedScrollingParentHelper(this)
    }
}