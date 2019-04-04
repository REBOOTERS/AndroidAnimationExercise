package home.smart.fly.animations.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import home.smart.fly.animations.R;

/**
 * @author: zhuyongging
 * @since: 2019-01-05
 */
public class CustomCoordinatorLayout extends CoordinatorLayout {

    private ViewDragHelper mViewDragHelper;
    private static final String TAG = "CustomCoordinatorLayout";

    public CustomCoordinatorLayout(@NonNull Context context) {
        super(context);
        initView();
    }

    public CustomCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                Log.e(TAG, "tryCaptureView: id ==" + view.getId());
                Log.e(TAG, "tryCaptureView: real id ==" + R.id.bottom_container);

                return view.getId() == R.id.bottom_container;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return top;
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return 1;
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return 1;
            }
        });

    }


    public void testSmoothSlide(boolean isReverse) {
        if (mViewDragHelper != null) {
            View child = getChildAt(1);
            if (child != null) {
                if (isReverse) {
                    mViewDragHelper.smoothSlideViewTo(child,
                            getLeft(), getTop());
                } else {
                    mViewDragHelper.smoothSlideViewTo(child,
                            getRight() - child.getWidth(),
                            getBottom() - child.getHeight());
                }
                invalidate();
            }

            View child1 = getChildAt(2);
            if (child1 != null) {
                if (isReverse) {
                    mViewDragHelper.smoothSlideViewTo(child1,
                            getLeft(), getTop());
                } else {
                    mViewDragHelper.smoothSlideViewTo(child1,
                            getRight() - child1.getWidth(),
                            getBottom() - child1.getHeight());
                }
                invalidate();
            }
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mViewDragHelper.processTouchEvent(ev);
        return super.onTouchEvent(ev);
    }
}
