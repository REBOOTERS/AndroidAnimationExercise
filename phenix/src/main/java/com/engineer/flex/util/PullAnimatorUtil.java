package com.engineer.flex.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by gavin
 * date 2018/6/12
 */
public class PullAnimatorUtil {

    /**
     * @param headerView
     * @param headerHeight
     * @param offsetY
     */
    public static void pullAnimator(View headerView, int headerHeight, int headerWidth, int offsetY, int maxHeight) {
        if (headerView == null) {
            return;
        }
        int pullOffset = (int) Math.pow(offsetY, 0.8);
        int newHeight = Math.min(maxHeight + headerHeight, pullOffset + headerHeight);
        int newWidth = (int) ((((float) newHeight / headerHeight)) * headerWidth);
        headerView.getLayoutParams().height = newHeight;
        headerView.getLayoutParams().width = newWidth;
        int margin = (newWidth - headerWidth) / 2;
        if (headerView.getParent() != null
                && headerView.getParent() instanceof RelativeLayout) {
            // TODO: gavin 2018/6/26  RelativeLayout会有问题，需要查明原因
            margin = 0;
        }
        headerView.setTranslationX(-margin);
        headerView.requestLayout();
    }

    /**
     * 高度重置动画
     *
     * @param headerView
     * @param headerHeight
     */
    public static void resetAnimator(final View headerView, final int headerHeight, int headerWidth) {
        if (headerView == null) {
            return;
        }
        ValueAnimator heightAnimator = ValueAnimator.ofInt(headerView.getLayoutParams().height, headerHeight);
        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int height = (int) animation.getAnimatedValue();
                headerView.getLayoutParams().height = height;
            }
        });
        ValueAnimator widthAnimator = ValueAnimator.ofInt(headerView.getLayoutParams().width, headerWidth);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue();
                headerView.getLayoutParams().width = width;
            }
        });
        ValueAnimator translationAnimator = ValueAnimator.ofInt((int) headerView.getTranslationX(), 0);
        translationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int translation = (int) animation.getAnimatedValue();
                headerView.setTranslationX(translation);
                headerView.requestLayout();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new FastOutSlowInInterpolator());
        set.setDuration(100);
        set.play(heightAnimator).with(widthAnimator).with(translationAnimator);
        set.start();

    }

    /**
     * 下拉时 刷新控件动画
     *
     * @param refreshView
     * @param offsetY
     * @param refreshViewHeight
     * @param maxRefreshPullHeight
     */
    public static void pullRefreshAnimator(View refreshView, int offsetY, int refreshViewHeight, int maxRefreshPullHeight) {
        if (refreshView == null) {
            return;
        }
        int pullOffset = (int) Math.pow(offsetY, 0.9);
        int newHeight = Math.min(maxRefreshPullHeight, pullOffset);
        refreshView.setTranslationY(-refreshViewHeight + newHeight);
        refreshView.setRotation(pullOffset);
        refreshView.requestLayout();
    }

    private static ObjectAnimator mRefreshingAnimator;

    /**
     * 刷新动画
     * 一直转圈圈
     *
     * @param refreshView
     */
    public static void onRefreshing(View refreshView) {
        float rotation = refreshView.getRotation();
        mRefreshingAnimator = ObjectAnimator.ofFloat(refreshView, "rotation", rotation, rotation + 360);
        mRefreshingAnimator.setDuration(1000);
        mRefreshingAnimator.setInterpolator(new LinearInterpolator());
        mRefreshingAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRefreshingAnimator.setRepeatCount(-1);
        mRefreshingAnimator.start();
    }

    /**
     * 重置刷新动画
     *
     * @param refreshView
     * @param refreshViewHeight
     */
    public static void resetRefreshView(View refreshView, int refreshViewHeight, Animator.AnimatorListener animatorListener) {
        if (mRefreshingAnimator != null) {
            mRefreshingAnimator.cancel();
        }
        float translation = refreshView.getTranslationY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(refreshView, "translationY", translation, -refreshViewHeight);
        animator.setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addListener(animatorListener);
        animator.start();
    }
}
