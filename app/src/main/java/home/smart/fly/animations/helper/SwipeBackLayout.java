package home.smart.fly.animations.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by GongWen on 17/8/24.
 */

public class SwipeBackLayout extends ViewGroup {
    private static final String TAG = "SwipeBackLayout";

    public static final int FROM_LEFT = 0;
    public static final int FROM_TOP = 1;
    public static final int FROM_RIGHT = 2;
    public static final int FROM_BOTTOM = 3;

    @IntDef({FROM_LEFT, FROM_TOP, FROM_RIGHT, FROM_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DirectionMode {
    }

    private int mDirectionMode = FROM_LEFT;

    private final ViewDragHelper mDragHelper;
    private View mDragContentView;
    private View innerScrollView;

    private int width, height;

    private int mTouchSlop;
    private float swipeBackFactor = 0.5f;
    private float swipeBackFraction;
    private int maskAlpha = 125;
    private float downX, downY;

    private int leftOffset = 0;
    private int topOffset = 0;

    public SwipeBackLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
        mTouchSlop = mDragHelper.getTouchSlop();
        setSwipeBackListener(defaultSwipeBackListener);

        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, com.gw.swipeback.R.styleable.SwipeBackLayout);
        setDirectionMode(a.getInt(com.gw.swipeback.R.styleable.SwipeBackLayout_directionMode, mDirectionMode));
        setSwipeBackFactor(a.getFloat(com.gw.swipeback.R.styleable.SwipeBackLayout_swipeBackFactor, swipeBackFactor));
        setMaskAlpha(a.getInteger(com.gw.swipeback.R.styleable.SwipeBackLayout_maskAlpha, maskAlpha));
        a.recycle();
    }

    public void attachToActivity(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);
        decorChild.setBackgroundColor(Color.TRANSPARENT);
        decorView.removeView(decorChild);
        addView(decorChild);
        decorView.addView(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (childCount > 1) {
            throw new IllegalStateException("SwipeBackLayout must contains only one direct child.");
        }
        int defaultMeasuredWidth = 0;
        int defaultMeasuredHeight = 0;
        int measuredWidth;
        int measuredHeight;
        if (childCount > 0) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            mDragContentView = getChildAt(0);
            defaultMeasuredWidth = mDragContentView.getMeasuredWidth();
            defaultMeasuredHeight = mDragContentView.getMeasuredHeight();
        }
        measuredWidth = View.resolveSize(defaultMeasuredWidth, widthMeasureSpec) + getPaddingLeft() + getPaddingRight();
        measuredHeight = View.resolveSize(defaultMeasuredHeight, heightMeasureSpec) + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) return;

        int left = getPaddingLeft() + leftOffset;
        int top = getPaddingTop() + topOffset;
        int right = left + mDragContentView.getMeasuredWidth();
        int bottom = top + mDragContentView.getMeasuredHeight();
        mDragContentView.layout(left, top, right, bottom);

        if (changed) {
            width = getWidth();
            height = getHeight();
        }
        innerScrollView = Util.findAllScrollViews(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(maskAlpha - (int) (maskAlpha * swipeBackFraction), 0, 0, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (innerScrollView != null && Util.contains(innerScrollView, downX, downY)) {
                    float distanceX = Math.abs(ev.getRawX() - downX);
                    float distanceY = Math.abs(ev.getRawY() - downY);
                    if (mDirectionMode == FROM_LEFT || mDirectionMode == FROM_RIGHT) {
                        if (distanceY > mTouchSlop && distanceY > distanceX) {
                            return super.onInterceptTouchEvent(ev);
                        }
                    } else if (mDirectionMode == FROM_TOP || mDirectionMode == FROM_BOTTOM) {
                        if (distanceX > mTouchSlop && distanceX > distanceY) {
                            return super.onInterceptTouchEvent(ev);
                        }
                    }
                }
                break;
        }
        boolean handled = mDragHelper.shouldInterceptTouchEvent(ev);
        return handled ? handled : super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void smoothScrollToX(int finalLeft) {
        if (mDragHelper.settleCapturedViewAt(finalLeft, getPaddingTop())) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void smoothScrollToY(int finalTop) {
        if (mDragHelper.settleCapturedViewAt(getPaddingLeft(), finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDragContentView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            leftOffset = getPaddingLeft();
            if (mDirectionMode == FROM_LEFT && !Util.canViewScrollRight(innerScrollView, downX, downY, false)) {
                leftOffset = Math.min(Math.max(left, getPaddingLeft()), width);
            } else if (mDirectionMode == FROM_RIGHT && !Util.canViewScrollLeft(innerScrollView, downX, downY, false)) {
                leftOffset = Math.min(Math.max(left, -width), getPaddingRight());
            }
            return leftOffset;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            topOffset = getPaddingTop();
            if (mDirectionMode == FROM_TOP && !Util.canViewScrollUp(innerScrollView, downX, downY, false)) {
                topOffset = Math.min(Math.max(top, getPaddingTop()), height);
            } else if (mDirectionMode == FROM_BOTTOM && !Util.canViewScrollDown(innerScrollView, downX, downY, false)) {
                topOffset = Math.min(Math.max(top, -height), getPaddingBottom());
            }
            return topOffset;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            left = Math.abs(left);
            top = Math.abs(top);
            switch (mDirectionMode) {
                case FROM_LEFT:
                case FROM_RIGHT:
                    swipeBackFraction = 1.0f * left / width;
                    break;
                case FROM_TOP:
                case FROM_BOTTOM:
                    swipeBackFraction = 1.0f * top / height;
                    break;
            }
            if (mSwipeBackListener != null) {
                mSwipeBackListener.onViewPositionChanged(mDragContentView, swipeBackFraction, swipeBackFactor);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            leftOffset = topOffset = 0;
            if (swipeBackFraction >= swipeBackFactor) {
                switch (mDirectionMode) {
                    case FROM_LEFT:
                        smoothScrollToX(width);
                        break;
                    case FROM_TOP:
                        smoothScrollToY(height);
                        break;
                    case FROM_RIGHT:
                        smoothScrollToX(-width);
                        break;
                    case FROM_BOTTOM:
                        smoothScrollToY(-height);
                        break;
                }
            } else {
                switch (mDirectionMode) {
                    case FROM_LEFT:
                    case FROM_RIGHT:
                        smoothScrollToX(getPaddingLeft());
                        break;
                    case FROM_BOTTOM:
                    case FROM_TOP:
                        smoothScrollToY(getPaddingTop());
                        break;
                }
            }
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (state == ViewDragHelper.STATE_IDLE) {
                if (mSwipeBackListener != null) {
                    if (swipeBackFraction == 0) {
                        mSwipeBackListener.onViewSwipeFinished(mDragContentView, false);
                    } else if (swipeBackFraction == 1) {
                        mSwipeBackListener.onViewSwipeFinished(mDragContentView, true);
                    }
                }
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return width;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return height;
        }
    }

    public void finish() {
        ((Activity) getContext()).finish();
    }

    public void setSwipeBackFactor(float swipeBackFactor) {
        if (swipeBackFactor > 1) {
            swipeBackFactor = 1;
        } else if (swipeBackFactor < 0) {
            swipeBackFactor = 0;
        }
        this.swipeBackFactor = swipeBackFactor;
    }

    public float getSwipeBackFactor() {
        return swipeBackFactor;
    }

    public void setMaskAlpha(int maskAlpha) {
        if (maskAlpha > 255) {
            maskAlpha = 255;
        } else if (maskAlpha < 0) {
            maskAlpha = 0;
        }
        this.maskAlpha = maskAlpha;
    }

    public int getMaskAlpha() {
        return maskAlpha;
    }

    public void setDirectionMode(@DirectionMode int direction) {
        mDirectionMode = direction;
    }

    public int getDirectionMode() {
        return mDirectionMode;
    }


    private OnSwipeBackListener mSwipeBackListener;

    private OnSwipeBackListener defaultSwipeBackListener = new OnSwipeBackListener() {
        @Override
        public void onViewPositionChanged(View mView, float swipeBackFraction, float swipeBackFactor) {
            invalidate();
        }

        @Override
        public void onViewSwipeFinished(View mView, boolean isEnd) {
            if (isEnd) {
                finish();
            }
        }
    };

    public void setSwipeBackListener(OnSwipeBackListener mSwipeBackListener) {
        this.mSwipeBackListener = mSwipeBackListener;
    }

    public interface OnSwipeBackListener {

        void onViewPositionChanged(View mView, float swipeBackFraction, float swipeBackFactor);

        void onViewSwipeFinished(View mView, boolean isEnd);
    }
}
