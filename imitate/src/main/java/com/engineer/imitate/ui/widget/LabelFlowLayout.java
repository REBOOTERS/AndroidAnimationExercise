package com.engineer.imitate.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

public class LabelFlowLayout extends ViewGroup {

    public LabelFlowLayout(Context context) {
        this(context, -1);
    }

    public LabelFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.maxLines = -1;
    }

    public LabelFlowLayout(Context context, int maxline) {
        super(context);
        this.maxLines = maxline;
    }

    public LabelFlowLayout(Context context, AttributeSet attrs, int maxline) {
        super(context, attrs);
        this.maxLines = maxline;
    }

    public LabelFlowLayout(Context context, AttributeSet attrs, int defStyle, int maxline) {
        super(context, attrs, defStyle);
        this.maxLines = maxline;
    }

    private int maxLines;

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewGroup.MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = View.MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = View.MeasureSpec.getMode(heightMeasureSpec);

        int width = 0, height = 0;// if wrap_content, mark

        int lineWidth = 0, lineHeight = 0;

        int cCount = getChildCount();

        int linesCount = 0;

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(lineWidth, childWidth);
                lineWidth = childWidth;

                height += lineHeight;
                lineHeight = childHeight;

                linesCount++;
                if (maxLines > 0 && linesCount > maxLines) {
                    height -= lineHeight;
                    break;
                }


            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == cCount - 1) {//last
                width = Math.max(width, lineWidth);
                height += lineHeight;

                linesCount++;
                if (maxLines > 0 && linesCount > maxLines) {
                    height -= lineHeight;
                    break;
                }
            }
        }
        setMeasuredDimension((modeWidth == View.MeasureSpec.EXACTLY) ? sizeWidth : width,
                (modeHeight == View.MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    private List<Integer> mLineHeights = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeights.clear();

        int width = getWidth();

        int lineWidth = 0, lineHeight = 0;

        List<View> lineViews = new ArrayList<View>();

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);

            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth(), childHeight = child.getMeasuredHeight();

            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {
                mLineHeights.add(lineHeight);
                mAllViews.add(lineViews);
                if (maxLines > 0 && mAllViews.size() > maxLines) {
                    mAllViews.remove(maxLines);
                    break;
                }
                lineWidth = 0;
                lineViews = new ArrayList<View>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }

        mLineHeights.add(lineHeight);
        mAllViews.add(lineViews);

        int left = 0, top = 0;

        int lineNums = mAllViews.size();

        if (maxLines > 0 && lineNums > maxLines) {
            lineNums = maxLines;
        }

        int showCount = 0;

        for (int i = 0; i < lineNums; i++) {
            lineViews = mAllViews.get(i);

            lineHeight = mLineHeights.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE)
                    continue;

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
                showCount++;
            }
            left = 0;
            top += lineHeight;
        }

        if (maxLines > 0) {
            for (int i = showCount; i < cCount; i++) {
                View dismiss = getChildAt(i);
                dismiss.setVisibility(GONE);
            }
        }

    }
}
