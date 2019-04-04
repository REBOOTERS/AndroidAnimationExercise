package home.smart.fly.animations.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import home.smart.fly.animations.R;


/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2018/4/26
 * desc   :
 * version: 1.0
 */
public class CustomTintImageView extends AppCompatImageView {

    private Drawable mDrawable;

    public CustomTintImageView(Context context) {
        super(context);
    }

    public CustomTintImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDrawable();
    }

    private void initDrawable() {

    }

    public void setChecked(boolean enable) {
        if (!enable) {
            setImageResource(R.drawable.ic_preview_radio_on);
            mDrawable = getDrawable();
            mDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        } else {
            setImageResource(R.drawable.ic_preview_radio_off);
            mDrawable = getDrawable();
            mDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        }
    }
}
