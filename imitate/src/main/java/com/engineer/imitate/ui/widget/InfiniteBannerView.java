package com.engineer.imitate.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.engineer.imitate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A banner view that can automatically or manually scroll in a loop
 * Created by Marksss on 2019/8/12.
 */
public class InfiniteBannerView extends ViewGroup {
    private Pager mPager = new Pager();
    private long mLoopInterval = 3000;
    private AbsBannerAdapter mAdapter;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private int mDividerWidth = 0;
    private float mForegroundWidthPercent = 1f; // 0.34<=percent<=0.1
    private boolean mIsReverse, mIsManuallyScrollLocked, mIsAutoScroll;
    private boolean mIsTouching, mIsAttachToWindow;
    private long mLastTouchTime, mLastScrollTime;
    private int mLastX, mLastDownX, mLastDownY;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop, mMaxVelocity;
    private OnItemClickListener mOnItemClickListener;
    private List<OnPageChangeListener> mOnPageChangeListeners = new ArrayList<>();
    private ScrollIntercepter mScrollIntercepter;
    private PageTransformer mPageTransformer;
    private SparseArray<View> mViewsMap = new SparseArray<>();

    public InfiniteBannerView(Context context) {
        super(context);
        init(context, null);
    }

    public InfiniteBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InfiniteBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InfiniteBannerView);
            mDividerWidth = a.getDimensionPixelSize(R.styleable.InfiniteBannerView_bannerDividerWidth, mDividerWidth);
            mPager.initPosition(a.getInt(R.styleable.InfiniteBannerView_bannerInitialPage, 0));
            mIsManuallyScrollLocked = a.getBoolean(R.styleable.InfiniteBannerView_bannerManuallyScrollLock, false);
            mIsAutoScroll = a.getBoolean(R.styleable.InfiniteBannerView_bannerAutoScroll, false);
            mIsReverse = a.getBoolean(R.styleable.InfiniteBannerView_bannerScrollReverse, false);
            float foregroundWidthPercent = a.getFloat(R.styleable.InfiniteBannerView_bannerForegroundWidthPercent, mForegroundWidthPercent);
            if (foregroundWidthPercent < 1f){
                setForegroundWidthPercent(foregroundWidthPercent);
            }
            String s = a.getString(R.styleable.InfiniteBannerView_bannerLoopInterval);
            if (s != null) {
                try {
                    mLoopInterval = Long.parseLong(s);
                } catch (NumberFormatException e) {
                    Log.e("InfiniteBannerView", e.getMessage());
                }
            }
            a.recycle();
        }
        mScroller = new Scroller(context);
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledPagingTouchSlop();
        mMaxVelocity = config.getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataObserver);
        }
        startLoopIfNeeded();
        mIsAttachToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        dispose();
        mIsAttachToWindow = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int maxHeight = sizeHeight - getPaddingTop() - getPaddingBottom();
        int childMaxWidth = (int) ((MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight()) * mForegroundWidthPercent) - mDividerWidth;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, MeasureSpec.makeMeasureSpec(childMaxWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
            height = Math.min(Math.max(height, child.getMeasuredHeight()), maxHeight);
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() == 0 || mAdapter == null) {
            return;
        }
        int outsideLength = (int) (getMeasuredWidth() * (1f - mForegroundWidthPercent) / 2);
        int periodLength = getPeriodLength(getMeasuredWidth());
        if (mPager.isSkippingPosition()) {
            // refresh the layout after reset
            int head = mPager.mCurrentPosition - getDepthWithCache();
            int tail = mPager.mCurrentPosition + getDepthWithCache();
            while (head <= tail) {
                if (head == tail) {
                    childLayout(getViewOnPosition(head), periodLength, outsideLength, head);
                } else {
                    childLayout(getViewOnPosition(head), periodLength, outsideLength, head);
                    childLayout(getViewOnPosition(tail), periodLength, outsideLength, tail);
                }
                head++;
                tail--;
            }
        } else if (mPager.isPreviousPosition()) {
            // scroll to the previous position
            childLayout(getViewOnPosition(mPager.mPositionLastRelayout + getDepthWithCache()),
                    periodLength, outsideLength, mPager.mPositionLastRelayout - getDepthWithCache() - 1);
        } else if (mPager.isNextPosition()) {
            // scroll to the next position
            childLayout(getViewOnPosition(mPager.mPositionLastRelayout - getDepthWithCache()),
                    periodLength, outsideLength, mPager.mPositionLastRelayout + getDepthWithCache() + 1);
        }
        mPager.recordWhenRelayout();
    }

    private void childLayout(final View child, int periodLength, int outsideLength, int page) {
        if (child == null) {
            return;
        }
        final int index = getLoopIndex(page);
        if (index >= 0) {
            int childX = outsideLength + page * periodLength;
            child.layout(
                    childX,
                    0,
                    childX + periodLength - mDividerWidth,
                    getMeasuredHeight());
            mAdapter.bind(child, index);
        } else {
            child.layout(0, 0, 0, 0);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int scrollX = mScroller.getCurrX();
            scrollTo(scrollX, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (getItemCount() > 0) {
            int periodLength = getPeriodLength(getWidth());
            int page = l / periodLength;
            int positionOffsetPixels = l % periodLength;
            if (l < 0){
                page -= 1;
                positionOffsetPixels += periodLength;
            }
            float positionOffset = (float) positionOffsetPixels / (float) periodLength;
            onPageScrolledInternal(page, positionOffset, positionOffsetPixels);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isManuallyScrollLocked()) {
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            }
            mVelocityTracker.addMovement(ev);
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished() && !isManuallyScrollLocked()) {
                    mScroller.abortAnimation();
                }
                mLastX = mLastDownX = x;
                mLastDownY = y;
                mIsTouching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isManuallyScrollLocked()) {
                    int dx = mLastX - x;
                    if (isCurrentScrollAllowed(mPager.mCurrentPosition, dx > 0)) {
                        scrollBy(dx, 0);
                    }
                    if (Math.abs(mLastDownX - x) > 20) {
                        requestDisallowInterceptTouchEvent(true);
                    }
                }
                mLastX = x;
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                if (x != mLastDownX) {
                    slowScrollToPage();
                }
                touchFinished();
                break;
            case MotionEvent.ACTION_UP:
                float outsideEachPercent = (1f - mForegroundWidthPercent) / 2;
                if (Math.abs(mLastDownX - x) < 20
                        && Math.abs(mLastDownY - y) < 20
                        && !performClick()) {
                    if (x > getWidth() * outsideEachPercent && x < getWidth() * (1 - outsideEachPercent)) {
                        handleClick();
                    } else if (!isManuallyScrollLocked()) {
                        if (x < getWidth() * outsideEachPercent) {
                            goPreviousPage();
                        } else {
                            goNextPage();
                        }
                    }
                } else if (!isManuallyScrollLocked()) {
                    handleScroll();
                }
                touchFinished();
                break;
            default:
                mIsTouching = false;
                break;
        }
        return true;
    }

    private void handleScroll() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int initVelocity = (int) mVelocityTracker.getXVelocity();
        if (initVelocity > mMaxVelocity) {
            goPreviousPage();
        } else if (initVelocity < -mMaxVelocity) {
            goNextPage();
        } else {
            slowScrollToPage();
        }
    }

    private void touchFinished() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        mIsTouching = false;
        mLastTouchTime = System.currentTimeMillis();
    }

    private void handleClick() {
        if (mOnItemClickListener != null && getItemCount() > 0) {
            int periodLength = getPeriodLength(getWidth());
            int page = getScrollX() / periodLength;
            if (getScrollX() % periodLength > periodLength / 2) {
                page++;
            } else if (getScrollX() % periodLength < -periodLength / 2){
                page--;
            }
            mOnItemClickListener.click(this, getLoopIndex(page));
        }
    }

    private void slowScrollToPage() {
        int periodLength = getPeriodLength(getWidth());
        int whichPage = (getScrollX() + periodLength / 2) / periodLength;
        scrollToPage(whichPage, false);
    }

    private int getPeriodLength(int width) {
        return (int) (width * mForegroundWidthPercent) + mDividerWidth;
    }

    private void scrollToPage(int indexPage, boolean reset) {
        int periodLength = getPeriodLength(getWidth());
        if (mPager.mCurrentPosition != indexPage) {
            long currentTime = System.currentTimeMillis();
            // If it scroll too fast, just go back to the current position or it looks weird.
            boolean changeable = Math.abs(getScrollX() - mPager.mCurrentPosition * periodLength) <= 2 * periodLength
                    && currentTime - mLastScrollTime > 300;
            if (changeable || reset) {
                mPager.updateCurrentPosition(indexPage);
                mLastScrollTime = currentTime;
                if (!mOnPageChangeListeners.isEmpty()) {
                    for (OnPageChangeListener onPageChangeListener : mOnPageChangeListeners) {
                        onPageChangeListener.onPageSelected(getLoopIndex(indexPage));
                    }
                }
            }
        }
        int dx = mPager.mCurrentPosition * periodLength - getScrollX();
        if (Math.abs(dx) < periodLength * 3 / 2) {
            mScroller.startScroll(getScrollX(), 0, dx, 0, Math.abs(dx));
            invalidate();
        } else {
            scrollTo(mPager.mCurrentPosition * periodLength, 0);
        }
        requestLayout();
    }

    public void onPageScrolledInternal(int position, float positionOffset, int positionOffsetPixels) {
        if (!mOnPageChangeListeners.isEmpty()) {
            for (OnPageChangeListener onPageChangeListener : mOnPageChangeListeners) {
                onPageChangeListener.onPageScrolled(getLoopIndex(position), positionOffset, positionOffsetPixels);
            }
        }
        if (mPageTransformer != null) {
            transform(position, 1f - positionOffset);
            transform(position + 1, positionOffset);
            transform(position - 1, 0);
            transform(position + 2, 0);
        }
    }

    private void transform(int position, float transformPos) {
        View child = getViewOnPosition(position);
        if (child != null && getWidth() > 0) {
            mPageTransformer.transformPage(child, transformPos);
        }
    }

    public boolean isReverse() {
        return mIsReverse;
    }

    /**
     * @param reverse
     * false:from right to left
     * true:from left to right
     */
    public void setReverse(boolean reverse) {
        mIsReverse = reverse;
    }

    private boolean isManuallyScrollLocked() {
        return mIsManuallyScrollLocked || getItemCount() <= 1;
    }

    /**
     * The manually scrolling will be locked or not
     * @param locked
     */
    public void setManuallyScrollLocked(boolean locked) {
        mIsManuallyScrollLocked = locked;
    }

    public boolean isAutoScroll() {
        return mIsAutoScroll;
    }

    /**
     * It will automatically scroll or not after the data is loaded
     * @param autoScroll
     */
    public void setAutoScroll(boolean autoScroll) {
        mIsAutoScroll = autoScroll;
    }

    /**
     * Scroll to the next position
     */
    public void goNextPage() {
        if (isCurrentScrollAllowed(mPager.mCurrentPosition, true)) {
            scrollToPage(mPager.mCurrentPosition + 1, false);
        }
    }

    /**
     * Scroll to the previous position
     */
    public void goPreviousPage() {
        if (isCurrentScrollAllowed(mPager.mCurrentPosition, false)) {
            scrollToPage(mPager.mCurrentPosition - 1, false);
        }
    }

    /**
     * Resume the scrolling animation
     */
    public void resumeLoop() {
        dispose();
        if (getItemCount() > 1) {
            loop();
        }
    }

    /**
     * Pause the scrolling animation
     */
    public void pauseLoop() {
        dispose();
    }

    private boolean isCurrentScrollAllowed(int position, boolean forward) {
        return mScrollIntercepter == null || !mScrollIntercepter.onScrollIntercept(position, forward);
    }

    private View getViewOnPosition(int position) {
        int index = getLoopIndex(getChildCount(), position);
        return mViewsMap.get(index);
    }

    private void loop(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mIsTouching && System.currentTimeMillis() - mLastTouchTime > mLoopInterval / 2) {
                    if (mIsReverse) {
                        goPreviousPage();
                    } else {
                        goNextPage();
                    }
                }
                loop();
            }
        }, mLoopInterval);
    }

    private void dispose() {
        mHandler.removeCallbacksAndMessages(null);
    }

    private void reset() {
        relayoutAllViews();
        mPager.reset();
        scrollToPage(mPager.mCurrentPosition, true);
        startLoopIfNeeded();
        post(new Runnable() {
            @Override
            public void run() {
                onPageScrolledInternal(mPager.mCurrentPosition, 0, 0);
            }
        });
    }

    private void startLoopIfNeeded() {
        if (getItemCount() > 1 && mIsAutoScroll) {
            resumeLoop();
        }
    }

    private void relayoutAllViews() {
        removeAllViewsInLayout();
        int count = getItemCount();
        for (int i = 0; i <= getDepthWithCache() * 2; i++) {
            View view = mViewsMap.get(i);
            if (view == null) {
                view = mAdapter.makeView(getContext());
                mViewsMap.put(i, view);
            }
            super.addView(view);
        }
    }

    /**
     * @return The layers that will be re-loaded
     */
    private int getDepthWithCache() {
        return mForegroundWidthPercent < 1f ? 3 : 2;
    }

    private int getLoopIndex(int index) {
        return getLoopIndex(getItemCount(), index);
    }

    private int getLoopIndex(int totalCount, int index) {
        if (totalCount == 0){
            return -1;
        }
        return index >= 0 ? index % totalCount : totalCount + (index + 1) % totalCount - 1;
    }

    public AbsBannerAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(AbsBannerAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        mAdapter = adapter;
        if (mIsAttachToWindow) {
            mAdapter.registerDataSetObserver(mDataObserver);
        }
        reset();
    }

    public ScrollIntercepter getScrollIntercepter() {
        return mScrollIntercepter;
    }

    public void setScrollIntercepter(ScrollIntercepter scrollIntercepter) {
        mScrollIntercepter = scrollIntercepter;
    }

    public void setPageTransformer(PageTransformer pageTransformer) {
        mPageTransformer = pageTransformer;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void addOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListeners.add(onPageChangeListener);
    }

    public void removeOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListeners.remove(onPageChangeListener);
    }

    public int getDividerWidth() {
        return mDividerWidth;
    }

    /**
     * Space between two items
     * @param dividerWidth
     */
    public void setDividerWidth(int dividerWidth) {
        mDividerWidth = dividerWidth;
    }

    public float getForegroundWidthPercent() {
        return mForegroundWidthPercent;
    }

    /**
     * The width
     * @param foregroundWidthPercent
     */
    public void setForegroundWidthPercent(float foregroundWidthPercent) {
        mForegroundWidthPercent = Math.max(Math.min(foregroundWidthPercent, 1f), 0.34f);
    }

    public long getLoopInterval() {
        return mLoopInterval;
    }

    /**
     * Time interval between automatically scrolling animations
     * @param loopInterval (millisecond)
     */
    public void setLoopInterval(long loopInterval) {
        mLoopInterval = loopInterval;
    }

    private int getItemCount() {
        return mAdapter == null ? 0 : mAdapter.getCount();
    }

    private DataSetObserver mDataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            reset();
        }

        @Override
        public void onInvalidated() {
            requestLayout();
        }
    };

    public static interface OnItemClickListener {
        void click(InfiniteBannerView view, int position);
    }

    public static interface OnPageChangeListener {
        // See OnPageChangeListener in ViewPager
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        void onPageSelected(int position);
    }

    public static interface ScrollIntercepter {
        /**
         * @param position current
         * @param forward true：from right to left；false：from left to right
         * @return intercept or not
         */
        boolean onScrollIntercept(int position, boolean forward);
    }

    public interface PageTransformer {
        // See PageTransformer in ViewPager
        void transformPage(View view, float arg);
    }

    private static class Pager {
        private int mInitialPosition = 0, mCurrentPosition;
        private int mPositionLastRelayout = Integer.MAX_VALUE;

        private void initPosition(int position) {
            mInitialPosition = position;
            mCurrentPosition = mInitialPosition;
        }

        private void reset() {
            mPositionLastRelayout = Integer.MAX_VALUE;
            mCurrentPosition = mInitialPosition;
        }

        private void updateCurrentPosition(int currentPosition) {
            mCurrentPosition = currentPosition;
        }

        private void recordWhenRelayout() {
            mPositionLastRelayout = mCurrentPosition;
        }

        private boolean isSkippingPosition() {
            return Math.abs(mPositionLastRelayout - mCurrentPosition) > 1;
        }

        private boolean isNextPosition() {
            return mCurrentPosition - mPositionLastRelayout == 1;
        }

        private boolean isPreviousPosition() {
            return mCurrentPosition - mPositionLastRelayout == -1;
        }
    }
}