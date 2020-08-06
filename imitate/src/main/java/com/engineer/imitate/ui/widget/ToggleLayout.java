package com.engineer.imitate.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.engineer.imitate.R;


public class ToggleLayout extends RelativeLayout {
    private Boolean isOpen = false;
    int min;
    int max;
    private ValueAnimator valueAnimator;
    private FrameLayout mContentView;
    private RelativeLayout rootView;
    private Boolean canToggle = false;
    private View mOpenBg;
    private View mToggleBtn;

    public enum Status {
        CLOSE,
        OPEN
    }

    public ToggleLayout(Context context) {
        super(context);
        initView();
    }

    public ToggleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ToggleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void addContentView(View view, Status defaultStatus) {
        mContentView.removeAllViews();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentView.setLayoutParams(params);
        mContentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int height = mContentView.getHeight();
                setMax(height);
                if (max - min < 5) {
                    canToggle = false;
                } else {
                    canToggle = true;
                }
                setState(defaultStatus);
                mContentView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });

        mContentView.addView(view);


    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }


    private void initView() {
        rootView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.toggle_layout_inner, this, true);
        mContentView = rootView.findViewById(R.id.content_view);
        mOpenBg = rootView.findViewById(R.id.toggle_bottom);
        mOpenBg.setOnClickListener(v -> {
            toggle();
        });

        mToggleBtn = rootView.findViewById(R.id.arrow);
    }

    public Status getState() {
        if (isOpen) return Status.OPEN;
        return Status.CLOSE;
    }

    /**
     * @param state 默认的状态
     */
    private void setState(Status state) {
        // 如果不能伸展
        if (!canToggle) {
            mOpenBg.setVisibility(INVISIBLE);
            mToggleBtn.setVisibility(GONE);
            return;
        }

        LayoutParams params;
        switch (state) {
            case OPEN:
                isOpen = true;
                params = new LayoutParams(LayoutParams.MATCH_PARENT, max);
                mContentView.setLayoutParams(params);
                break;
            case CLOSE:
                isOpen = false;
                params = new LayoutParams(LayoutParams.MATCH_PARENT, min);
                mContentView.setLayoutParams(params);
                break;
        }
        mToggleBtn.setVisibility(VISIBLE);
        mOpenBg.setVisibility(VISIBLE);
        mContentView.setVisibility(VISIBLE);
    }

    public void toggle() {
        if (!canToggle) return;

//        mCallBack.onToggleBtnClick(isOpen ? Status.OPEN : Status.CLOSE);

        valueAnimator = ValueAnimator.ofInt(isOpen ? max : min, isOpen ? min : max);
        valueAnimator.addUpdateListener(animation -> {
            int height = (int) animation.getAnimatedValue();
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
            mContentView.setLayoutParams(params);
        });
        valueAnimator.setDuration(300);

        toggleBtn();


        valueAnimator.start();

        isOpen = !isOpen;
//        mCallBack.onStatusChange(isOpen ? Status.OPEN : Status.CLOSE);
    }

    private void toggleBtn() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mToggleBtn, "rotation", isOpen ? 0f : 180f, isOpen ? 180f : 0f);
        anim.setDuration(300);
        anim.start();
    }


}

