/*
 * FixMultiViewPager 2016-12-26
 * Copyright (c) 2016 suzeyu Co.Ltd. All right reserved
 */
package home.smart.fly.animations.activity.gallery;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Author :  suzeyu
 * Time   :  2016-12-26  上午1:41
 * ClassDescription : 对多点触控场景时, {@link android.support.v4.view.ViewPager#onInterceptTouchEvent(MotionEvent)}中
 *                  pointerIndex = -1. 发生IllegalArgumentException: pointerIndex out of range 处理
 */
public class FixMultiViewPager extends ViewPager {
    private static final String TAG = FixMultiViewPager.class.getSimpleName();

    public FixMultiViewPager(Context context) {
        super(context);
    }

    public FixMultiViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "onInterceptTouchEvent() ", ex);
            ex.printStackTrace();
        }
        return false;
    }

}
