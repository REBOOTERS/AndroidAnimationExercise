package home.smart.fly.animations.widget;

import android.content.Context;
import androidx.annotation.Nullable;
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
        setOrientation(HORIZONTAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "width==" + MeasureSpec.getSize(widthMeasureSpec));
        Log.e(TAG, "height=" + MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout: l=" + l);
        Log.e(TAG, "onLayout: t=" + t);
        Log.e(TAG, "onLayout: r=" + r);
        Log.e(TAG, "onLayout: b=" + b);
        int count = 0;
        int childCount = getChildCount();
        int left = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child != null && child.getVisibility() == VISIBLE) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                Log.e(TAG, "onLayout: i==" + i + " childLeft==" + child.getLeft() + " childWidth==" + childWidth);


                if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                    left = child.getLeft() + (childWidth / 4) * count;
                } else if (getLayoutDirection() == LAYOUT_DIRECTION_LTR) {
                    left = child.getLeft() - (childWidth / 4) * count;
                }
                child.layout(left, child.getTop(), left + childWidth, child.getTop() + childHeight);

                count++;
            }
        }
        Log.e(TAG, "onLayout: left==" + left);

        Log.e(TAG, "onLayout: width ==" + getMeasuredWidth());
        Log.e(TAG, "onLayout: height ==" + getMeasuredHeight());

        setMeasuredDimension(getMeasuredWidth() - left, getMeasuredHeight());

    }
}
