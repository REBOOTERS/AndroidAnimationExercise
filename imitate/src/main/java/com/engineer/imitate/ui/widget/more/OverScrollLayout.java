package com.engineer.imitate.ui.widget.more;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 仿豆瓣书影音详情页横向滑动加载更多
 * <p>
 * Created by fjz on 2020/4/21.
 */
public class OverScrollLayout extends RelativeLayout {

    private static final String TAG = "OverScrollLayout";

    /**
     * 回弹时间
     */
    private static final int ANIM_DURATION = 400;

    /**
     * 弹出View的最大宽度
     */
    private static final int OVER_SCROLL_SIZE = 120;

    /**
     * 弹出TextView变化的宽度
     */
    private static final int OVER_SCROLL_STATE_CHANGE_SIZE = 96;

    /**
     * 阻尼
     */
    private static final float DAMPING = .3f;

    /**
     *
     */
    private static final float TEXT_DAMPING = .2f;

    /**
     * 弹出文本
     */
    private static final String OVER_SCROLL_TEXT = "查看更多";

    /**
     * 弹出变化文本
     */
    private static final String OVER_SCROLL_CHANGE_TEXT = "释放查看";

    /**
     * 子View
     */
    private RecyclerView mChildView;

    /**
     * 弹出View
     */
    private OverScrollView mOverScrollView;

    /**
     * 弹出TextView
     */
    private TextView mOverScrollTextView;

    /**
     * 子View初始位置
     */
    private Rect originalRect = new Rect();

    /**
     * TextView初始位置
     */
    private Rect textOriginalRect = new Rect();

    /**
     * 触摸时的横向偏移
     */
    private float startX;

    /**
     * 是否移动
     */
    private boolean isMoved;

    /**
     *
     */
    private boolean isRecyclerResult;

    /**
     * 释放回调
     */
    private OnOverScrollReleaseListener mOnOverScrollReleaseListener;

    public OverScrollLayout(Context context) {
        this(context, null);
    }

    public OverScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mOverScrollView = new OverScrollView(getContext());
        mOverScrollTextView = new TextView(getContext());
        mOverScrollTextView.setEms(1);
        mOverScrollTextView.setLineSpacing(0, 0.8f);
        mOverScrollTextView.setText(OVER_SCROLL_TEXT);
        mOverScrollTextView.setTextSize(11f);
        mOverScrollTextView.setTextColor(Color.parseColor("#CDCDCD"));
        addView(mOverScrollView);
        addView(mOverScrollTextView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mChildView == null) {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof RecyclerView) {
                    mChildView = (RecyclerView) getChildAt(i);
                }
            }
        }
        mChildView.measure(MeasureSpec.makeMeasureSpec(
                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.AT_MOST));
        mOverScrollView.measure(MeasureSpec.makeMeasureSpec(OVER_SCROLL_SIZE,
                MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.AT_MOST));
        mOverScrollTextView.measure(MeasureSpec.makeMeasureSpec(OVER_SCROLL_SIZE,
                MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(
                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.AT_MOST));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = mChildView.getMeasuredWidth();
        int height = mChildView.getMeasuredHeight();
        mChildView.layout(l, t, l + width, t + height);

        mOverScrollView.layout(r - mOverScrollView.getMeasuredWidth(), t, r, b);

        int textTop = (int) ((t + b) / 2f - mOverScrollTextView.getMeasuredHeight() / 2f);
        int textBottom = (int) ((t + b) / 2f + mOverScrollTextView.getMeasuredHeight() / 2f);
        mOverScrollTextView.layout(r, textTop, r + mOverScrollTextView.getMeasuredWidth(), textBottom);

        //设置初始位置
        originalRect.set(l, t, t + width, t + height);
        textOriginalRect.set(r, textTop, r + mOverScrollTextView.getMeasuredWidth(), textBottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (isMoved) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        requestDisallowInterceptTouchEvent(true);//禁止父控件拦截事件

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //记录按下时的X
                startX = ev.getX();
            case MotionEvent.ACTION_MOVE:
                float nowX = ev.getX();
                int scrollX = (int) ((nowX - startX) * DAMPING);
                if (isCanPullLeft() && scrollX < 0) {
                    int absScrollX = Math.abs(scrollX);
                    int textScrollX = Math.abs((int) ((nowX - startX) * TEXT_DAMPING));
                    mChildView.layout(originalRect.left - absScrollX, originalRect.top, originalRect.right - absScrollX, originalRect.bottom);
                    if (absScrollX < OVER_SCROLL_SIZE) {
                        if (absScrollX >= OVER_SCROLL_STATE_CHANGE_SIZE) {
                            mOverScrollTextView.setText(OVER_SCROLL_CHANGE_TEXT);
                        } else {
                            mOverScrollTextView.setText(OVER_SCROLL_TEXT);
                        }
                        mOverScrollView.startOverScroll(OVER_SCROLL_SIZE - absScrollX, originalRect.top, OVER_SCROLL_SIZE + absScrollX, originalRect.bottom);
                        mOverScrollTextView.layout(textOriginalRect.left - textScrollX, textOriginalRect.top, textOriginalRect.right - textScrollX, textOriginalRect.bottom);
                    }
                    isMoved = true;
                    isRecyclerResult = false;
                    return true;
                } else {
                    startX = ev.getX();
                    isMoved = false;
                    isRecyclerResult = true;
                    recoverLayout();
                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_UP:
                if (isMoved) {
                    recoverLayout();
                }

                if (isRecyclerResult) {
                    return super.dispatchTouchEvent(ev);
                } else {
                    return true;
                }
            default:
                return super.dispatchTouchEvent(ev);
        }
    }

    /**
     * 子View回归原位
     */
    private void recoverLayout() {
        if (!isMoved) {
            return;//如果没有移动布局，则跳过执行
        }

        TranslateAnimation rvAnim = new TranslateAnimation(mChildView.getLeft() - originalRect.left, 0, 0, 0);
        rvAnim.setDuration(ANIM_DURATION);
        mChildView.startAnimation(rvAnim);
        mChildView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);

        TranslateAnimation overTextAnim = new TranslateAnimation(mOverScrollView.getLeft(), originalRect.left, 0, 0);
        mOverScrollTextView.startAnimation(overTextAnim);
        mOverScrollTextView.layout(originalRect.right, originalRect.top, originalRect.right + mOverScrollTextView.getWidth(), originalRect.bottom);

        ValueAnimator overViewAnim = ValueAnimator.ofInt(OVER_SCROLL_SIZE - mOverScrollView.left, 0);
        overViewAnim.setDuration(ANIM_DURATION);
        overViewAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int offset = (int) animation.getAnimatedValue();
                mOverScrollView.startOverScroll(OVER_SCROLL_SIZE - offset, originalRect.top, OVER_SCROLL_SIZE + offset, originalRect.bottom);
            }
        });
        overViewAnim.start();

        //回调
        if (OVER_SCROLL_SIZE - mOverScrollView.left >= OVER_SCROLL_STATE_CHANGE_SIZE) {
            if (mOnOverScrollReleaseListener != null) {
                mOnOverScrollReleaseListener.onRelease();
            }
        }
    }

    /**
     * 判断是否可以左拉
     */
    private boolean isCanPullLeft() {
        final RecyclerView.Adapter adapter = mChildView.getAdapter();

        if (adapter == null) {
            return true;
        }

        final int lastItemPosition = adapter.getItemCount() - 1;
        final int lastVisiblePosition = ((LinearLayoutManager) mChildView.getLayoutManager()).findLastVisibleItemPosition();

        if (lastVisiblePosition >= lastItemPosition) {
            final int childIndex = lastVisiblePosition - ((LinearLayoutManager) mChildView.getLayoutManager()).findFirstVisibleItemPosition();
            final int childCount = mChildView.getChildCount();
            final int index = Math.min(childIndex, childCount - 1);
            final View lastVisibleChild = mChildView.getChildAt(index);
            if (lastVisibleChild != null) {
                return lastVisibleChild.getRight() + ((MarginLayoutParams) lastVisibleChild.getLayoutParams()).rightMargin
                        <= mChildView.getRight() - mChildView.getLeft();
            }
        }

        return false;
    }

    public void setOnOverScrollReleaseListener(OnOverScrollReleaseListener onOverScrollReleaseListener) {
        mOnOverScrollReleaseListener = onOverScrollReleaseListener;
    }

    public interface OnOverScrollReleaseListener {
        void onRelease();
    }

    private class OverScrollView extends View {

        private Paint mOverScrollPaint;

        int left, top, right, bottom;

        public OverScrollView(Context context) {
            super(context);

            mOverScrollPaint = new Paint();
            mOverScrollPaint.setStyle(Paint.Style.FILL);
            mOverScrollPaint.setAntiAlias(true);
            mOverScrollPaint.setColor(Color.parseColor("#F5F5F5"));
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawArc((float) left, (float) top, (float) right, (float) bottom, 0f, 360f, false, mOverScrollPaint);
        }

        public void startOverScroll(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            invalidate();
        }
    }
}
