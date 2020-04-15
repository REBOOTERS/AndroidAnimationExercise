package home.smart.fly.animations.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

/**
 * @authro: Rookie
 * @since: 2019-01-21
 * <p>
 * TabLayout 的 onScrollChanged 被限制到 API 23 ,为了兼容，接口处理一下
 */
public class InnerTabLayout extends TabLayout {

    private OnScrollChangeInnerListener mOnScrollChangeInnerListener;

    public void setOnScrollChangeInnerListener(OnScrollChangeInnerListener onScrollChangeInnerListener) {
        mOnScrollChangeInnerListener = onScrollChangeInnerListener;
    }

    public InnerTabLayout(Context context) {
        super(context);
    }

    public InnerTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeInnerListener != null) {
            mOnScrollChangeInnerListener.onScrollChange(this, l, t, oldl, oldt);
        }
    }


    public interface OnScrollChangeInnerListener {
        void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
