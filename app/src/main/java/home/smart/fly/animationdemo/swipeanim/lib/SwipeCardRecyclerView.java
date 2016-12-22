package home.smart.fly.animationdemo.swipeanim.lib;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;


public class SwipeCardRecyclerView extends RecyclerView {
    private float mTopViewX;
    private float mTopViewY;

    private float mTopViewOffsetX = 0;
    private float mTopViewOffsetY = 0;

    private float mTouchDownX;
    private float mTouchDownY;

    private float mBorder = dip2px(120);

    private ItemTouchListener mTouchListener;

    private FrameLayout mDecorView;
    private int[] mDecorViewLocation = new int[2];


    private Map<View, Animator> mAnimatorMap;


    public SwipeCardRecyclerView(Context context) {
        super(context);
        initView();
    }

    public SwipeCardRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SwipeCardRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        mDecorView = (FrameLayout) ((Activity) getContext()).getWindow().getDecorView();
        mDecorView.getLocationOnScreen(mDecorViewLocation);
        mAnimatorMap = new HashMap<>();
    }

    public void setRemovedListener(ItemTouchListener listener) {
        mTouchListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (getChildCount() == 0) {
            return super.onTouchEvent(e);
        }
        View topView = getChildAt(getChildCount() - 1);
        float touchX = e.getX();
        float touchY = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mAnimatorMap.containsKey(topView)) {
                    mAnimatorMap.get(topView).cancel();
                    mAnimatorMap.remove(topView);
                    mTopViewOffsetX = topView.getX();
                    mTopViewOffsetY = topView.getY();
                } else {
                    mTopViewX = topView.getX();
                    mTopViewY = topView.getY();
                    mTopViewOffsetX = 0;
                    mTopViewOffsetY = 0;
                }
                mTouchDownX = touchX;
                mTouchDownY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = touchY - mTouchDownY;
                if (Math.abs(dy) > 100) {
                    mTouchListener.hide(dy);
                }
                topView.setY(mTopViewY + dy + mTopViewOffsetY);
                break;
            case MotionEvent.ACTION_UP:
                mTouchDownX = 0;
                mTouchDownY = 0;
                touchUp(topView);
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 更新下一个View的宽高
     */
    private void updateNextItem() {
        if (getChildCount() < 1) {
            return;
        }


        View nextView = getChildAt(getChildCount() - 1);
        nextView.setVisibility(VISIBLE);
        mTouchListener.show();

    }

    /**
     * 手指抬起时触发动画
     *
     * @param view
     */
    private void touchUp(final View view) {
        float targetX = 0;
        float targetY = 0;
        boolean del = false;
        if (Math.abs(view.getY() - mTopViewY) < mBorder) {
            targetX = mTopViewX;
            targetY = mTopViewY;
            mTouchListener.show();
        } else if (view.getY() - mTopViewY > mBorder) {
            del = true;
            targetY = getScreenHeight() * 2;
            mTouchListener.onDownRemoved();
        } else {
            del = true;
//            targetX = -view.getWidth() - getScreenWidth();
            targetY = -view.getWidth() - getScreenHeight();
            mTouchListener.onUpRemoved();
        }
        View animView = view;
        TimeInterpolator interpolator;
        if (del) {
            animView = getMirrorView(view);
            interpolator = new LinearInterpolator();
        } else {
            interpolator = new OvershootInterpolator();
        }
        final boolean finalDel = del;
        final View finalAnimView = animView;
        animView.animate()
                .setDuration(500)
                .x(targetX)
                .y(targetY)
                .setInterpolator(interpolator)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        if (!finalDel) {
                            mAnimatorMap.put(finalAnimView, animation);
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (finalDel) {
                            try {
                                updateNextItem();
                                mDecorView.removeView(finalAnimView);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            mAnimatorMap.remove(finalAnimView);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }


                });

    }

    private int dip2px(float dip) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics()
        );
    }


    private float getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 创建镜像View替代原有顶部View展示删除动画
     *
     * @param view
     * @return
     */
    private ImageView getMirrorView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        final ImageView mirrorView = new ImageView(getContext());
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        mirrorView.setImageBitmap(bitmap);
        view.setDrawingCacheEnabled(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        mirrorView.setAlpha(view.getAlpha());
        view.setVisibility(GONE);
        ((SwipeCardAdapter) getAdapter()).delTopItem();
        mirrorView.setX(locations[0] - mDecorViewLocation[0]);
        mirrorView.setY(locations[1] - mDecorViewLocation[1]);
        mDecorView.addView(mirrorView, params);
        return mirrorView;
    }
}
