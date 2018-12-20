package home.smart.fly.animations.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
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

    //<editor-fold desc="construct">
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

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View view, int i) {
                Log.e(TAG, "tryCaptureView: view==" + view);
                Log.e(TAG, "tryCaptureView: i   ==" + i);
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return top;
            }
        });
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
