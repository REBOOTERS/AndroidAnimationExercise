package home.smart.fly.animations.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author: rookie
 * @since: 2018-12-12
 */
public class DragHelperView extends LinearLayout {
    private static final String TAG = "DragHelperView";
    private ViewDragHelper mViewDragHelper;

    //<editor-fold desc="construct" >
    public DragHelperView(Context context) {
        super(context);
        initView();
    }

    public DragHelperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DragHelperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    //</editor-fold>

    private int mOriginLeft;
    private int mOriginTop;

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                Log.e(TAG, "tryCaptureView: view==" + view);
                Log.e(TAG, "tryCaptureView: i   ==" + i);
                return true;
            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                mOriginLeft = capturedChild.getLeft();
                mOriginTop = capturedChild.getTop();
            }


            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return top;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                mViewDragHelper.settleCapturedViewAt(mOriginLeft, mOriginTop);
                invalidate();
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);
                Log.e(TAG, "onEdgeDragStarted: edgeFlags==" + edgeFlags);
                Log.e(TAG, "onEdgeDragStarted: pointerId==" + pointerId);
                mViewDragHelper.captureChildView(getChildAt(0), pointerId);
            }
        });

        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
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
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
