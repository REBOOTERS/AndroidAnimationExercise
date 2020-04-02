package com.engineer.imitate.ui.widget.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.engineer.imitate.R;

public class LoadingImagesView extends LinearLayout {

    public static final String TAG = LoadingImagesView.class.getSimpleName();

    private final int DEFAULT_RESOURCE_ID = 0;

    private int imagesArrayId;
    private int imageIndex = 0;
    private int totalDuration = 1500;
    private int direction = 1;
    private int maxWidth = 0;
    private int dividerColor;
    private int backgroundColor;
    private boolean isBuilt = false;

    private ImageView firstImage;
    private ImageView secondImage;
    private LinearLayout separatorLayout;

    private ValueAnimator animator;
    private TypedArray imagesArray;


    public LoadingImagesView(Context context) {
        super(context);
        init(context);
    }

    public LoadingImagesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setViewAttributes(context, attrs, 0);
        init(context);
    }

    public LoadingImagesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setViewAttributes(context, attrs, defStyleAttr);
        init(context);
    }

    private void setViewAttributes(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LoadingImagesView, defStyleAttr, 0);

        imagesArrayId = array.getResourceId(R.styleable.LoadingImagesView_imagesArray, DEFAULT_RESOURCE_ID);
        totalDuration = array.getInt(R.styleable.LoadingImagesView_animDuration, totalDuration);
        dividerColor = array.getColor(R.styleable.LoadingImagesView_dividerColor, Color.WHITE);
        backgroundColor = array.getColor(R.styleable.LoadingImagesView_backgroundColor, Color.parseColor("#999999"));

        if (imagesArrayId != DEFAULT_RESOURCE_ID) {
            imagesArray = getResources().obtainTypedArray(imagesArrayId);
        }
        array.recycle();
    }

    private void init(Context context) {
        inflate(context, R.layout.loading_images_layout, this);
        build();
    }

    private void build() {
        if (isBuilt) {
            return;
        }
        isBuilt = true;

        setViews();
        setAnimator();
    }

    private void setViews() {
        if (imagesArrayId == DEFAULT_RESOURCE_ID) {
            Log.e(TAG, "Images must be initialized before it can be used. You can use in XML like this: (app:imagesArray=app:imagesArray=\"@array/loading_car_images\") ");
            return;
        }
        firstImage = findViewById(R.id.first_image);
        secondImage = findViewById(R.id.second_image);
        separatorLayout = findViewById(R.id.separator_layout);
        View divider = findViewById(R.id.divider);

        firstImage.setImageResource(imagesArray.getResourceId(getImageIndex(), DEFAULT_RESOURCE_ID));
        secondImage.setImageResource(imagesArray.getResourceId(getImageIndex(), DEFAULT_RESOURCE_ID));
        firstImage.setBackgroundColor(backgroundColor);
        secondImage.setBackgroundColor(backgroundColor);
        divider.setBackgroundColor(dividerColor);

        firstImage.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        maxWidth = firstImage.getMeasuredWidth();

    }

    private void setAnimator() {
        if (animator != null)
            animator.removeAllUpdateListeners();

        animator = ValueAnimator.ofFloat(0, maxWidth);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(totalDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                separatorLayout.getLayoutParams().width = getWidth(animator);
                separatorLayout.requestLayout();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                direction = -direction;
                if (direction < 0) {
                    firstImage.setImageResource(imagesArray.getResourceId(getImageIndex(), DEFAULT_RESOURCE_ID));
                } else {
                    secondImage.setImageResource(imagesArray.getResourceId(getImageIndex(), DEFAULT_RESOURCE_ID));
                }
            }
        });

        animator.start();
    }

    private int getWidth(ValueAnimator animator) {
        int calculatedWidth;
        if (direction < 0) {
            calculatedWidth = maxWidth - (int) ((float) animator.getAnimatedValue());
        } else {
            calculatedWidth = (int) ((float) animator.getAnimatedValue());
        }
        return calculatedWidth;
    }

    private int getImageIndex() {
        return imageIndex++ % imagesArray.length();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null)
            animator.pause();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (animator != null) {
            animator.resume();
        }
    }

    public void setDividerInterpolator(TimeInterpolator interpolator) {
        if (animator != null) {
            animator.setInterpolator(interpolator);
        }
    }
}