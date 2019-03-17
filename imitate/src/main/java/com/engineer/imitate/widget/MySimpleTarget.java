package com.engineer.imitate.widget;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

/**
 * @author: zhuyongging
 * @since: 2019-03-17
 */
public class MySimpleTarget extends CustomViewTarget {
    /**
     * Constructor that defaults {@code waitForLayout} to {@code false}.
     *
     * @param view
     */
    public MySimpleTarget(@NonNull View view) {
        super(view);
    }

    @Override
    protected void onResourceCleared(@Nullable Drawable placeholder) {

    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {

    }

    @Override
    public void onResourceReady(@NonNull Object resource, @Nullable Transition transition) {

    }
}
