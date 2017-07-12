package home.smart.fly.animations.customview.gallery;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import home.smart.fly.animations.utils.Tools;

/**
 * Created by Rookie on 2017/7/12.
 */

public class DragViewLayout extends FrameLayout {
    private static final String TAG = "DragViewLayout";

    private View dragView;
    private float downY;
    private float downX;
    private int mScaledTouchSlop;

    private float slopDistances;

    private int mScreenHeight;


    public DragViewLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DragViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScreenHeight = Tools.getScreenHeight(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dragView = getChildAt(0);
        Log.e(TAG, "init: " + dragView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = event.getY();
                downX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getX() - downX;
                float deltaY = event.getY() - downY;

                if (Math.abs(deltaY) > mScaledTouchSlop || Math.abs(deltaX) > mScaledTouchSlop) {

                    if (Math.abs(deltaY) > Math.abs(deltaX)) {
                        if (deltaY > 0) {
                            slopDistances = slopDistances + deltaX;

                            float scale = 1 - (slopDistances * 2 / mScreenHeight);

                            ViewCompat.setScaleX(dragView, scale);
                            ViewCompat.setScaleY(dragView, scale);
                            return true;
                        }
                    }

                    downX = event.getX();
                    downY = event.getY();
                }


        }
        return super.onTouchEvent(event);
    }
}
