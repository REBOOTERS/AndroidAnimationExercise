package com.engineer.imitate.ui.widget.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java8.util.Optional;

/**
 * @author: zhuyongging
 * @since: 2019-01-16
 */
public class DragView extends View {

    private static final String TAG = "DragView";

    public DragView(Context context) {
        super(context);
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private onGuesterActionListener mOnGuesterActionListener;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        intercept(true);
        return super.dispatchTouchEvent(event);
    }


    float y = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
//        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                y = event.getY();
                intercept(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                intercept(true);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float deltaY = event.getY() - y;
                Log.e(TAG, "onTouchEvent: deltaY=" + deltaY);

                if (deltaY > 0) {
                    Log.e(TAG, "onTouchEvent: down");
                    Optional.ofNullable(mOnGuesterActionListener)
                            .ifPresent(onGuesterActionListener::down);
                } else {

                    Log.e(TAG, "onTouchEvent: up");
                    Optional.ofNullable(mOnGuesterActionListener)
                            .ifPresent(onGuesterActionListener::up);
                }
                intercept(false);
        }
        return super.onTouchEvent(event);

    }

    private void intercept(boolean intercept) {
        Optional.ofNullable(getParent())
                .ifPresent(parent -> parent.requestDisallowInterceptTouchEvent(intercept));
    }


    public void setOnGuesterActionListener(onGuesterActionListener onGuesterActionListener) {
        mOnGuesterActionListener = onGuesterActionListener;
    }

    public interface onGuesterActionListener {
        void up();

        void down();
    }
}
