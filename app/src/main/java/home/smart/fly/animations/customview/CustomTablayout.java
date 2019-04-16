package home.smart.fly.animations.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import home.smart.fly.animations.R;

/**
 * @author: zhuyongging
 * @since: 2019-04-15
 */
public class CustomTablayout extends FrameLayout {
    public CustomTablayout(@NonNull Context context) {
        this(context, null);
    }

    public CustomTablayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTablayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_tablayout, null);
        addView(view);
    }
}
