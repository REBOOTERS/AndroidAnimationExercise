package home.smart.fly.animations.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import home.smart.fly.animations.R;

/**
 * @author: zhuyongging
 * @since: 2019-01-08
 */
public class CustomTabLayout extends FrameLayout {


    private TabLayout mTabLayout;

    public CustomTabLayout(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_tab_layout_root,null);
        mTabLayout = view.findViewById(R.id.tabs);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabTextColors(R.color.green,R.color.black);
        addView(view);
    }
}
