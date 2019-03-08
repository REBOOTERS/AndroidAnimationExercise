package home.smart.fly.animations.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @author: zhuyongging
 * @since: 2019-03-07
 */
public class OverLapLayout extends LinearLayout {

    private static final String TAG = "OverLapLayout";

    public OverLapLayout(Context context) {
        this(context, null);
    }

    public OverLapLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverLapLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutDirection(HORIZONTAL);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int count = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != null && child.getVisibility() == VISIBLE) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                Log.e(TAG, "onLayout: i==" + i + " childLeft==" + child.getLeft() + " childWidth==" + childWidth);

                int left = child.getLeft() + (childWidth / 4) * count;
                int start = child.getLeft() - (childWidth / 4) * count;
                if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                    child.layout(left, child.getTop(), left + childWidth, child.getTop() + childHeight);
                } else if (getLayoutDirection() == LAYOUT_DIRECTION_LTR) {
//                    child.layout(left, child.getTop(), left + childWidth, child.getTop() + childHeight);
//                    child.layout(start, child.getTop(), start + childWidth, child.getTop() + childHeight);
                    child.layout(start, child.getTop(), start + childWidth, child.getTop() + childHeight);
                }
                count++;
            }
        }
    }
}
