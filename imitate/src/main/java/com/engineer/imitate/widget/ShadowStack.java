package com.engineer.imitate.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhuyongging
 * @since: 2019-03-21
 */
public class ShadowStack implements View.OnTouchListener {

    private Context mContext;
    private ViewGroup mContainer;

    private View mTargetView;
    private int mTargetViewHeight;
    private int mTargetViewWidth;

    private Bitmap mFakeView; // copy bitmap of TargetView
    private int[] mOriginLocation = new int[2];

    private int mShadowCount;
    private List<ImageView> mChildViews = new ArrayList<>();



    public void setContainer(ViewGroup container) {
        mContainer = container;
    }

    public void setTargetView(View targetView) {
        mTargetView = targetView;
    }


    private void initTargetView() {
        mTargetViewWidth = mTargetView.getWidth();
        mTargetViewHeight = mTargetView.getHeight();
        mTargetView.setDrawingCacheEnabled(true);
        mTargetView.buildDrawingCache();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
