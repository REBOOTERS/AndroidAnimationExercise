package home.smart.fly.animations.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.FrameLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yyf @ JarvisGG.io
 * @since 10-16-2018
 * @function 无缝拖拽 parentView，假如 childView 可以滚动，犟 touch dispatch 给它，假如不可以，当前会自己消化 touch 事件
 */
public class NestedTouchScrollingLayout extends FrameLayout implements NestedScrollingParent {

    private static final String TAG = "NestedTouchScrolling";

    @IntDef({
            SheetDirection.ALL,
            SheetDirection.TOP,
            SheetDirection.BOTTOM
    })

    @Retention(RetentionPolicy.SOURCE)
    public @interface SheetDirection {
        int ALL = 0x000;
        int TOP = 0x001;
        int BOTTOM = 0x002;
    }

    private View mChildView;

    private ObjectAnimator mTransYAnim;
    private ObjectAnimator currentAnimator;

    private VelocityTracker velocityTracker;

    private float minFlingVelocity;

    private float mTouchSlop;

    private float mDownY;

    private float mDownX;

    private float mDownSheetTranslation;

    private float mOriginTranslate = 0;

    /**
     * 假如 cover BottomSheet 场景，sheetView ** 会 ** 从哪个方向弹出
     */
    private @SheetDirection int mSheetDirection = SheetDirection.ALL;

    /**
     * 手指向上阻尼值
     */
    private float mDampingUp = 1;

    /**
     * 手指向下阻尼值
     */
    private float mDampingDown = 1;

    /**
     ************* 键盘收起，导致 reLayout，getHeight 发生改变，所以一开始就锁定高度
     */
    private int mTouchParentViewOriginMeasureHeight = 0;

    /**
     * 针对包含的子 View 为 webview 的情况
     */
    private int mWebViewContentHeight;

    /**
     * 横向拖拽 dispatchTouch 给 childView
     */
    private boolean mParentOwnsTouch;

    /**
     * 竖向拖拽 NestedTouchScrollingLayout 是否消化 touch（根据 childView (canScrollUp or canScrollDown)）
     */
    private boolean isHoldTouch = true;

    private float mSheetTranslation;

    /**
     * 是否允许左右滑动，下发滑动事件
     */
    private boolean isLeftorRightTouchLimit = true;

    private boolean isFingerHolderTouch = false;

    private boolean isParentDispatchTouchEvent = true;

    private List<INestChildScrollChange> mNestChildScrollChangeCallbacks;

    private Map<Integer, OnNestOffsetChangedListener> mOnOffsetChangedListener = new ArrayMap<>();


    private final Property<NestedTouchScrollingLayout, Float> SHEET_TRANSLATION = new Property<NestedTouchScrollingLayout, Float>(Float.class, "sheetTranslation") {
        @Override
        public Float get(NestedTouchScrollingLayout object) {
            return mTouchParentViewOriginMeasureHeight - object.mSheetTranslation;
        }

        @Override
        public void set(NestedTouchScrollingLayout object, Float value) {
            object.seAnimtTranslation(value);
        }
    };

    public void setLeftorRightTouchLimit(boolean leftorRightTouchLimit) {
        this.isLeftorRightTouchLimit = leftorRightTouchLimit;
    }

    public interface INestChildScrollChange {
        /**
         * nestChild scroll change
         * @param deltaY
         */
        void onNestChildScrollChange(float deltaY);

        void onNestChildScrollRelease(float deltaY, int velocityY);

        void onFingerUp(float velocityY);

        void onNestChildHorizationScroll(MotionEvent event, float deltaX, float deltaY);
    }

    public NestedTouchScrollingLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public NestedTouchScrollingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public NestedTouchScrollingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NestedTouchScrollingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mNestChildScrollChangeCallbacks = new ArrayList<>();
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        post(new Runnable() {
            @Override
            public void run() {
                mTouchParentViewOriginMeasureHeight = NestedTouchScrollingLayout.this.getMeasuredHeight();
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("child must be 1!!!");
        }
        mChildView = getChildAt(0);
    }

    @Override
    public void addView(View child) {
        if (getChildCount() >= 1) {
            throw new IllegalStateException("child must be 1!!!");
        }
        mChildView = child;
        super.addView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if (getChildCount() >= 1) {
            throw new IllegalStateException("child must be 1!!!");
        }
        deepSearchView(child);
        mChildView = child;
        super.addView(child, params);
    }

    class OnNestOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {
        int offsetY = 0;
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int offsetY) {
            this.offsetY = offsetY;
        }

        public int getOffsetY() {
            return Math.abs(offsetY);
        }
    }

    private void deepSearchView(View parentView) {
        if (parentView instanceof AppBarLayout) {
            OnNestOffsetChangedListener listener = new OnNestOffsetChangedListener();
            mOnOffsetChangedListener.put(parentView.hashCode(), listener);
            ((AppBarLayout) parentView).addOnOffsetChangedListener(listener);
        }

        if (parentView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) parentView).getChildCount(); i++) {
                deepSearchView(((ViewGroup) parentView).getChildAt(i));
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        velocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearNestScrollChildCallback();
        velocityTracker.clear();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isParentDispatchTouchEvent) {
            return true;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float velocityY = 0;
        if (getChildAt(0) == null || !isParentDispatchTouchEvent) {
            return super.onTouchEvent(event);
        }
        if (isAnimating()) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            mOriginTranslate = mChildView.getTranslationY();
            mTouchParentViewOriginMeasureHeight = this.getMeasuredHeight();

            mParentOwnsTouch = false;
            mDownY = event.getY();
            mDownX = event.getX();
            mSheetTranslation = mTouchParentViewOriginMeasureHeight - mOriginTranslate;
            mDownSheetTranslation = mSheetTranslation;
            velocityTracker.clear();

            isFingerHolderTouch = true;

            if (mChildView instanceof WebView) {
                mWebViewContentHeight = (int) (((WebView)mChildView).getContentHeight() * ((WebView)mChildView).getScale());
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            isFingerHolderTouch = false;
            velocityTracker.computeCurrentVelocity(1000);
            velocityY = velocityTracker.getYVelocity();
            notifyOnFingerUp(velocityY);
        }

        getParent().requestDisallowInterceptTouchEvent(true);

        velocityTracker.addMovement(event);

        float maxSheetTranslation = mTouchParentViewOriginMeasureHeight;

        float deltaY = mDownY - event.getY();
        float deltaX = mDownX - event.getX();

        if ((!canScrollLeft(getChildAt(0), event.getX(), event.getX()) &&
                !canScrollRight(getChildAt(0), event.getX(), event.getY()) &&
                !isLeftorRightTouchLimit) ||
                (event.getAction() == MotionEvent.ACTION_UP ||
                        event.getAction() == MotionEvent.ACTION_CANCEL)) {
            interceptHorizontalTouch(event, deltaX, deltaY);
        }

        if (!mParentOwnsTouch) {
            mParentOwnsTouch = Math.abs(deltaY) > 0 && Math.abs(deltaY) > Math.abs(deltaX);

            if (mParentOwnsTouch) {

                mDownY = event.getY();
                mDownX = event.getX();
                deltaY = 0;
                deltaX = 0;
            }
        }

        float newSheetTranslation = mDownSheetTranslation + deltaY;

        if (mParentOwnsTouch) {


            if (isHoldTouch && !isChildCanScroll(event, deltaY) && deltaY != 0) {
                mDownY = event.getY();
                velocityTracker.clear();
                isHoldTouch = false;
                newSheetTranslation = mSheetTranslation;

                MotionEvent cancelEvent = MotionEvent.obtain(event);
                cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                getChildAt(0).dispatchTouchEvent(cancelEvent);
                cancelEvent.recycle();
            }

            if (!isHoldTouch && isChildCanScroll(event, deltaY) && deltaY != 0) {
                setSheetTranslation(maxSheetTranslation);
                isHoldTouch = true;
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    MotionEvent downEvent = MotionEvent.obtain(event);
                    downEvent.setAction(MotionEvent.ACTION_DOWN);
                    getChildAt(0).dispatchTouchEvent(downEvent);
                    downEvent.recycle();
                }
            }

            if (isHoldTouch && deltaY != 0) {
                event.offsetLocation(0, mSheetTranslation - mTouchParentViewOriginMeasureHeight);
                getChildAt(0).dispatchTouchEvent(event);
            } else {
                setSheetTranslation(newSheetTranslation);

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    isHoldTouch = true;
                    getParent().requestDisallowInterceptTouchEvent(false);

                    if (Math.abs(velocityY) < minFlingVelocity) {
                        if (mSheetTranslation > getHeight() / 2) { } else { }
                    } else {
                        if (velocityY < 0) { } else { }
                    }
                    notifyNestScrollChildReleaseCallback((int) velocityY);
                }
            }
        } else {
            event.offsetLocation(0, mSheetTranslation - mTouchParentViewOriginMeasureHeight);
            getChildAt(0).dispatchTouchEvent(event);
        }
        return true;
    }

    private boolean isChildCanScroll(MotionEvent event, float deltaY) {
        boolean fingerDown = deltaY - mOriginTranslate < 0;
        boolean canScrollDown = canScrollDown(getChildAt(0), event.getX(), event.getY() + (mSheetTranslation - getHeight()), false);
        boolean fingerUp = deltaY - mOriginTranslate > 0;
        boolean canScrollUp = canScrollUp(getChildAt(0), event.getX(), event.getY() + (mSheetTranslation - getHeight()), false);
        return (fingerDown && canScrollUp) || (fingerUp && canScrollDown);
    }

    /**
     * child can scroll
     * @param view
     * @param x
     * @param y
     * @param lockRect 是否开启 touch 所动在当前 view 区域
     * @return
     */
    protected boolean canScrollUp(View view, float x, float y, boolean lockRect) {

        if (view instanceof WebView) {
            return canWebViewScrollUp();
        }
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                int childLeft = child.getLeft() - view.getScrollX();
                int childTop = child.getTop() - view.getScrollY();
                int childRight = child.getRight() - view.getScrollX();
                int childBottom = child.getBottom() - view.getScrollY();
                boolean intersects = x > childLeft && x < childRight && y > childTop && y < childBottom;
                if ((!lockRect || intersects)
                        && canScrollUp(child, x - childLeft, y - childTop, lockRect)) {
                    return true;
                }
            }
        }

        if (view instanceof CoordinatorLayout &&
                ((CoordinatorLayout) view).getChildCount() > 0 &&
                ((CoordinatorLayout) view).getChildAt(0) instanceof AppBarLayout) {
            AppBarLayout layout = (AppBarLayout) ((CoordinatorLayout) view).getChildAt(0);
            OnNestOffsetChangedListener listener = mOnOffsetChangedListener.get(layout.hashCode());
            if (listener != null) {
                if (listener.getOffsetY() < layout.getMeasuredHeight() && listener.getOffsetY() > 0) {
                    return true;
                }
            }
        }

        return view.canScrollVertically(-1);
    }

    /**
     * child can scroll
     * @param view
     * @param x
     * @param y
     * @param lockRect 是否开启 touch 所动在当前 view 区域
     * @return
     */
    protected boolean canScrollDown(View view, float x, float y, boolean lockRect) {
        if (view instanceof WebView) {
            return canWebViewScrollDown();
        }
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                int childLeft = child.getLeft() - view.getScrollX();
                int childTop = child.getTop() - view.getScrollY();
                int childRight = child.getRight() - view.getScrollX();
                int childBottom = child.getBottom() - view.getScrollY();
                boolean intersects = x > childLeft && x < childRight && y > childTop && y < childBottom;
                if ((!lockRect || intersects)
                        && canScrollDown(child, x - childLeft, y - childTop, lockRect)) {
                    return true;
                }
            }
        }

        if (view instanceof CoordinatorLayout &&
                ((CoordinatorLayout) view).getChildCount() > 0 &&
                ((CoordinatorLayout) view).getChildAt(0) instanceof AppBarLayout) {
            AppBarLayout layout = (AppBarLayout) ((CoordinatorLayout) view).getChildAt(0);
            OnNestOffsetChangedListener listener = mOnOffsetChangedListener.get(layout.hashCode());
            if (listener != null) {
                if (listener.getOffsetY() < layout.getMeasuredHeight() && listener.getOffsetY() > 0) {
                    return true;
                }
            }
        }

        return view.canScrollVertically(1);
    }

    private boolean canScrollLeft(View view, float x, float y) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                int childLeft = child.getLeft() - view.getScrollX();
                int childTop = child.getTop() - view.getScrollY();
                int childRight = child.getRight() - view.getScrollX();
                int childBottom = child.getBottom() - view.getScrollY();
                boolean intersects = x > childLeft && x < childRight && y > childTop && y < childBottom;
                if (intersects && canScrollLeft(child, x - childLeft, y - childTop)) {
                    return true;
                }
            }
        }
        return view.canScrollHorizontally(-1);
    }

    private boolean canScrollRight(View view, float x, float y) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                int childLeft = child.getLeft() - view.getScrollX();
                int childTop = child.getTop() - view.getScrollY();
                int childRight = child.getRight() - view.getScrollX();
                int childBottom = child.getBottom() - view.getScrollY();
                boolean intersects = x > childLeft && x < childRight && y > childTop && y < childBottom;
                if (intersects && canScrollRight(child, x - childLeft, y - childTop)) {
                    return true;
                }
            }
        }
        return view.canScrollHorizontally(1);
    }

    /**
     * 规避 contentHeight 异步变化
     * @return
     */
    private boolean canWebViewScrollUp() {
        final int offset = mChildView.getScrollY();
        final int range = mWebViewContentHeight - mChildView.getHeight();
        if (range == 0) {
            return false;
        }
        return offset > 0;
    }

    /**
     * 规避 contentHeight 异步变化
     * @return
     */
    private boolean canWebViewScrollDown() {
        final int offset = mChildView.getScrollY();
        final int range = mWebViewContentHeight - mChildView.getHeight();
        if (range == 0) {
            return false;
        }
        return offset < range - 3;
    }


    private void setSheetTranslation(float newTranslation) {
        this.mSheetTranslation = newTranslation;
        int bottomClip = (int) (mTouchParentViewOriginMeasureHeight - Math.ceil(mSheetTranslation));
        setTranslation(bottomClip);
    }

    private void seAnimtTranslation(float transY) {
        this.mSheetTranslation = mTouchParentViewOriginMeasureHeight - transY;
        setTranslation(transY);
    }

    private void setTranslation(float transY) {
        if (mSheetDirection == SheetDirection.BOTTOM && transY < 0) {
            mChildView.setTranslationY(0);
            return;
        }
        if (mSheetDirection == SheetDirection.TOP && transY > 0) {
            mChildView.setTranslationY(0);
            return;
        }
        transY = transY > 0 ? transY * mDampingDown : transY * mDampingUp;
        notifyNestScrollChildChangeCallback(transY);
        if (mChildView != null) {
            mChildView.setTranslationY(transY);
        }
        if (transY == 0) {
            mDownSheetTranslation = mTouchParentViewOriginMeasureHeight;
            mDownY -= mOriginTranslate;
            mOriginTranslate = 0;
        }
    }

    /**
     * 还原到某一位置
     * @param target
     */
    public void recover(int target) {
        recover(target, null);
    }

    public void recover(int target, final Runnable runnable) {
        recover(target, runnable, 300);
    }

    public void recover(int target, final Runnable runnable, int time) {
        currentAnimator = ObjectAnimator.ofFloat(this, SHEET_TRANSLATION, target);
        currentAnimator.setDuration(time);
        currentAnimator.setInterpolator(new DecelerateInterpolator(1.6f));
        currentAnimator.addListener(new CancelDetectionAnimationListener() {
            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                if (!canceled) {
                    currentAnimator = null;
                }
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        currentAnimator.start();

    }

    private void interceptHorizontalTouch(MotionEvent event, float deltaX, float deltaY) {
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            notifyNestScrollChildHorizontalCallback(event, deltaX, deltaY);
            return;
        }
        if (Math.abs(deltaX) > mTouchSlop * 8 && Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > 0) {
            notifyNestScrollChildHorizontalCallback(event, deltaX, deltaY);
        }
    }

    private boolean isAnimating() {
        return currentAnimator != null;
    }

    private static class CancelDetectionAnimationListener extends AnimatorListenerAdapter {

        protected boolean canceled;

        @Override
        public void onAnimationCancel(Animator animation) {
            canceled = true;
        }

    }

    private void onActionMove(MotionEvent event) {
        float distance = countDragDistanceFromMotionEvent(event);
        mChildView.setTranslationY(distance);
    }

    private void onActionRelease(MotionEvent event) {
        float distance = countDragDistanceFromMotionEvent(event);
        if (mTransYAnim != null && mTransYAnim.isRunning()) {
            mTransYAnim.cancel();
        }

        mTransYAnim = ObjectAnimator.ofFloat(mChildView, View.TRANSLATION_Y,
                mChildView.getTranslationY(), 0.0F);
        mTransYAnim.setDuration(200L);
        mTransYAnim.setInterpolator(PathInterpolatorCompat.create(0.4F, 0.0F, 0.2F, 1.0F));

        mTransYAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

            }
        });

        mTransYAnim.start();
    }

    public void registerNestScrollChildCallback(INestChildScrollChange childScrollChange) {
        if (!mNestChildScrollChangeCallbacks.contains(childScrollChange)) {
            mNestChildScrollChangeCallbacks.add(childScrollChange);
        }
    }

    public void removeNestScrollChildCallback(INestChildScrollChange childScrollChange) {
        if (mNestChildScrollChangeCallbacks.contains(childScrollChange)) {
            mNestChildScrollChangeCallbacks.remove(childScrollChange);
        }
    }

    public void clearNestScrollChildCallback() {
        mNestChildScrollChangeCallbacks.clear();
    }

    private void notifyNestScrollChildChangeCallback(float detlaY) {
        for (INestChildScrollChange change : mNestChildScrollChangeCallbacks) {
            change.onNestChildScrollChange(detlaY);
        }
    }

    private void notifyNestScrollChildReleaseCallback(int velocityY) {
        for (INestChildScrollChange change : mNestChildScrollChangeCallbacks) {
            change.onNestChildScrollRelease(getChildAt(0).getTranslationY(), velocityY);
        }
    }

    private void notifyNestScrollChildHorizontalCallback(MotionEvent event, float deltaX, float deltaY) {
        for (INestChildScrollChange change : mNestChildScrollChangeCallbacks) {
            change.onNestChildHorizationScroll(event, deltaX, deltaY);
        }
    }

    private void notifyOnFingerUp(float velocityY) {
        for (INestChildScrollChange change : mNestChildScrollChangeCallbacks) {
            change.onFingerUp(velocityY);
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    private float countDragDistanceFromMotionEvent(@NonNull MotionEvent event) {
        float distance = event.getRawY();
        return distance;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }


    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
    }

    @Override
    public int getNestedScrollAxes() {
        return super.getNestedScrollAxes();
    }

    public boolean isFingerHolderTouch() {
        return isFingerHolderTouch;
    }

    public float getMinFlingVelocity() {
        return minFlingVelocity;
    }

    /**
     * 展开
     */
    public void expand() {
        expand(null);
    }

    /**
     * 中间态
     * @param offset
     */
    public void peek(int offset) {
        peek(offset, null);
    }

    /**
     * 隐藏
     */
    public void hiden() {
        hiden(null);
    }

    public void expand(Runnable runnable) {
        recover(0, runnable);
    }

    public void peek(int offset, Runnable runnable) {
        recover(offset, runnable);
    }

    public void hiden(Runnable runnable) {
        recover(getMeasuredHeight(), runnable);
    }

    /**
     * bottomSheet 方向
     * @param direction
     */
    public void setSheetDirection(@SheetDirection int direction) {
        mSheetDirection = direction;
    }

    /**
     * 下阻尼
     * @param mDampingDown
     */
    public void setDampingDown(float mDampingDown) {
        this.mDampingDown = mDampingDown;
    }

    /**
     * 上阻尼
     * @param mDampingUp
     */
    public void setDampingUp(float mDampingUp) {
        this.mDampingUp = mDampingUp;
    }

    /**
     * 是否开启拦截
     * @param b
     */
    public void setParentDispatchTouchEvent(boolean b) {
        isParentDispatchTouchEvent = b;
    }

    /**
     * 动画是否正在执行
     * @return
     */
    public boolean isAniming() {
        if (currentAnimator == null) {
            return false;
        }
        return currentAnimator.isRunning();
    }
}
