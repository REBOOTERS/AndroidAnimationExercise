package com.engineer.imitate.ui.widget.more;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;


public class DZStickyNavLayouts extends LinearLayout implements NestedScrollingParent {

    private NestedScrollingParentHelper mParentHelper;
    private View mHeaderView;
    private AnimatorView mFooterView;
    private RecyclerView mChildView;
    // 解决多点触控问题
    private boolean isRunAnim;
    public static int maxWidth = 0;
    private static final int DRAG = 2;

    public interface OnStartActivityListener {
        void onStart();
    }

    private OnStartActivityListener mLinster;

    public void setOnStartActivity(OnStartActivityListener l) {
        mLinster = l;
    }

    public DZStickyNavLayouts(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeaderView = new View(context);
        mHeaderView.setBackgroundColor(0xffFFFFFF);
        mFooterView = new AnimatorView(context);
        mFooterView.setBackgroundColor(0xffFFFFFF);
        maxWidth = dp2Px(context, 120);
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOrientation(LinearLayout.HORIZONTAL);

        if (getChildAt(0) instanceof RecyclerView) {
            mChildView = (RecyclerView) getChildAt(0);
            LayoutParams layoutParams = new LayoutParams(maxWidth, LayoutParams.MATCH_PARENT);
            addView(mHeaderView, 0, layoutParams);
            addView(mFooterView, getChildCount(), layoutParams);
            // 左移
            scrollBy(maxWidth, 0);

            mChildView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // 保证动画状态中 子view不能滑动
                    return isRunAnim;
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mChildView != null) {
            ViewGroup.LayoutParams params = mChildView.getLayoutParams();
            params.width = getMeasuredWidth();
        }
    }

    /**
     * 必须要复写 onStartNestedScroll后调用
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    /**
     * 返回true代表处理本次事件
     * 在执行动画时间里不能处理本次事件
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return target instanceof RecyclerView && !isRunAnim;
    }

    /**
     * 复位初始位置
     * scrollTo 移动到指定坐标
     * scrollBy 在原有坐标上面移动
     */
    @Override
    public void onStopNestedScroll(View target) {
        mParentHelper.onStopNestedScroll(target);
        // 如果不在RecyclerView滑动范围内
        if (maxWidth != getScrollX()) {
            startAnimation(new ProgressAnimation());
        }

        if (getScrollX() > maxWidth + maxWidth / 2 && mLinster != null) {
            mLinster.onStart();
        }
    }

    /**
     * 回弹动画
     */
    private class ProgressAnimation extends Animation {

        private ProgressAnimation() {
            isRunAnim = true;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            scrollBy((int) ((maxWidth - getScrollX()) * interpolatedTime), 0);
            if (interpolatedTime == 1) {
                isRunAnim = false;
                mFooterView.setRelease();
            }
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            setDuration(300);
            setInterpolator(new AccelerateInterpolator());
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }

    /**
     * @param dx       水平滑动距离
     * @param dy       垂直滑动距离
     * @param consumed 父类消耗掉的距离
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        getParent().requestDisallowInterceptTouchEvent(true);
        // dx>0 往左滑动 dx<0往右滑动
        //System.out.println("dx:" + dx + "=======getScrollX:" + getScrollX() + "==========canScrollHorizontally:" + !ViewCompat.canScrollHorizontally(target, -1));
        boolean hiddenLeft = dx > 0 && getScrollX() < maxWidth && !ViewCompat.canScrollHorizontally(target, -1);
        boolean showLeft = dx < 0 && !ViewCompat.canScrollHorizontally(target, -1);
        boolean hiddenRight = dx < 0 && getScrollX() > maxWidth && !ViewCompat.canScrollHorizontally(target, 1);
        boolean showRight = dx > 0 && !ViewCompat.canScrollHorizontally(target, 1);
        if (hiddenLeft || showLeft || hiddenRight || showRight) {
            scrollBy(dx / DRAG, 0);
            consumed[0] = dx;
        }

        if (hiddenRight || showRight) {
            mFooterView.setRefresh(dx / DRAG);
        }

        // 限制错位问题
        if (dx > 0 && getScrollX() > maxWidth && !ViewCompat.canScrollHorizontally(target, -1)) {
            scrollTo(maxWidth, 0);
        }
        if (dx < 0 && getScrollX() < maxWidth && !ViewCompat.canScrollHorizontally(target, 1)) {
            scrollTo(maxWidth, 0);
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    /**
     * 子view是否可以有惯性 解决右滑时快速左滑显示错位问题
     *
     * @return true不可以  false可以
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        // 当RecyclerView在界面之内交给它自己惯性滑动
        return getScrollX() != maxWidth;
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    /**
     * 限制滑动 移动x轴不能超出最大范围
     */
    @Override
    public void scrollTo(int x, int y) {
        if (x < 0) {
            x = 0;
        } else if (x > maxWidth * 2) {
            x = maxWidth * 2;
        }
        super.scrollTo(x, y);
    }

    private int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
