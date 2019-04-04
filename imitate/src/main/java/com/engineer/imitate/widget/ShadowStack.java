package com.engineer.imitate.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.engineer.imitate.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: zhuyongging
 * @since: 2019-03-21
 */
public class ShadowStack<T extends View> implements View.OnTouchListener {

    private static final int TIME = 150;

    private Context mContext;
    private ViewGroup mContainer;

    private T mTargetView;
    private int mTargetViewHeight;
    private int mTargetViewWidth;

    // copy bitmap of TargetView
    private Bitmap mFakeView;
    private int[] mOriginLocation = new int[2];

    private int mShadowCount = 8;
    private List<ImageView> mChildViews = new ArrayList<>();

    // statusbar height + titlebar height
    private int mContentTopInWindow = 0;
    private VelocityTracker mVelocityTracker;


    public ShadowStack(Context context) {
        mContext = context;
    }


    public void setTargetView(T targetView) {
        mTargetView = targetView;
    }


    public void setContainer(ViewGroup container) {
        mVelocityTracker = VelocityTracker.obtain();

        mContainer = container;
        initTargetView();
        if (mTargetViewWidth == 0 || mTargetViewHeight == 0 || mFakeView == null) {
            mTargetView.getViewTreeObserver().addOnWindowFocusChangeListener(
                    new ViewTreeObserver.OnWindowFocusChangeListener() {
                        @Override
                        public void onWindowFocusChanged(boolean hasFocus) {
                            if (hasFocus) {
                                mTargetView.getViewTreeObserver().removeOnWindowFocusChangeListener(this);
                                initTargetView();
                                attachToRootLayoutInternal();
                            }
                        }
                    });
        } else {
            attachToRootLayoutInternal();
        }

        mTargetView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (mTargetViewWidth > 0 && mTargetViewHeight > 0 && mFakeView != null) {
                updateChildViewsPosition();
            }
        });
    }


    private void initTargetView() {
        mTargetViewWidth = mTargetView.getWidth();
        mTargetViewHeight = mTargetView.getHeight();
        mFakeView = ViewUtils.getBitmapFromView(mTargetView);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void attachToRootLayoutInternal() {

        if (!mChildViews.isEmpty()) {
            for (View child : mChildViews) {
                mContainer.removeView(child);
            }
            mChildViews.clear();
        }
        //
        updateTargetViewPosition();
        for (int i = 0; i < mShadowCount; i++) {
            ImageView shadow;

            if (mTargetView instanceof CircleImageView) {
                shadow = new CircleImageView(mContext);
            } else {
                shadow = new ImageView(mContext);
            }
            mContainer.addView(shadow);
            shadow.getLayoutParams().width = mTargetViewWidth;
            shadow.getLayoutParams().height = mTargetViewHeight;
            shadow.setImageBitmap(mFakeView);
            shadow.setTranslationX(mOriginLocation[0]);
            shadow.setTranslationY(mOriginLocation[1]);
            float alpha = i == mShadowCount - 1 ? 1.0f : 0.5f / mShadowCount * (i + 1);
            shadow.setAlpha(alpha);
            mChildViews.add(shadow);
            if (i == mShadowCount - 1) {
                shadow.setOnTouchListener(this);
                shadow.setElevation(mTargetView.getElevation());
                shadow.setOnClickListener(v -> mTargetView.performClick());
            }
        }
        mTargetView.setVisibility(View.INVISIBLE);
    }


    private void updateTargetViewPosition() {
        mTargetView.getLocationOnScreen(mOriginLocation);
        int statusBarHeight = getStatusBarHeight(mContext);
        int titleBarHeight = 0;
        if (mContext instanceof Activity) {
            // actionbar height
            Activity activity = (Activity) mContext;
            ViewGroup content = activity.findViewById(android.R.id.content);
            titleBarHeight = content.getTop();
            // statusBar height compat
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            boolean isTranslucentStatus = (params.flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
            boolean isFitSystemWindows = true;
            if (null != content.getChildAt(0)) {
                isFitSystemWindows = content.getChildAt(0).getFitsSystemWindows();
            }

            if (isTranslucentStatus && !isFitSystemWindows) {
                statusBarHeight = 0;
            }
        }
        mContentTopInWindow = titleBarHeight + statusBarHeight;
        int top = mOriginLocation[1] - mContentTopInWindow;
        mOriginLocation[1] = top;
    }

    private void updateChildViewsPosition() {
        updateTargetViewPosition();
        for (View child : mChildViews) {
            if (child.getTranslationX() == mOriginLocation[0] && child.getTranslationY() == mOriginLocation[1]) {
                break;
            }
            child.setTranslationX(mOriginLocation[0]);
            child.setTranslationY(mOriginLocation[1]);
            child.requestLayout();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int velocityX;
        int velocityY;
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                updateTargetViewPosition();
            case MotionEvent.ACTION_MOVE:
                if (event.getEventTime() - event.getDownTime() >= TIME) {
                    float accX;
                    float accY;
                    mVelocityTracker.computeCurrentVelocity(1000, 1000f);
                    velocityX = (int) mVelocityTracker.getXVelocity();
                    velocityY = (int) mVelocityTracker.getXVelocity();
                    accX = velocityX / 1000f;
                    accY = velocityY / 1000f;

                    dragView(event.getRawX() - mTargetViewWidth / 2f,
                            event.getRawY() - mTargetViewHeight / 2f - mContentTopInWindow,
                            velocityX, velocityY, accX, accY);
                    mChildViews.get(mShadowCount - 1).setClickable(false);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mChildViews.get(mShadowCount - 1).setClickable(true);
                if (event.getEventTime() - event.getDownTime() >= TIME) {
                    releaseView();
                    return true;
                }
            default:
                break;
        }

        return false;
    }

    private void releaseView() {
        final Interpolator interpolator = new OvershootInterpolator();
        final int duration = 700;
        for (int i = 0; i < mShadowCount; i++) {
            final View childI = mChildViews.get(i);
            final int delay = 100 * (mShadowCount - 1 - i);
            childI.postDelayed(() -> childI.animate()
                    .translationX(mOriginLocation[0])
                    .translationY(mOriginLocation[1])
                    .setDuration(duration)
                    .setInterpolator(interpolator)
                    .start(), delay);
        }
    }

    private void dragView(float v, float v1, int velocityX, int velocityY, float accX, float accY) {
        for (int i = 0; i < mShadowCount; i++) {
            View view = mChildViews.get(i);
            long delay = 100 * (mShadowCount - 1 - i);
            view.postDelayed(() -> {
                view.setTranslationX(v);
                view.setTranslationY(v1);
                view.requestLayout();
            }, delay);
        }
    }


    private int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
