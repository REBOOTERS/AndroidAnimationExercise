package home.smart.fly.animations.customview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import home.smart.fly.animations.R;

/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2018/4/25
 * desc   :
 * version: 1.0
 */
public class CustomImageView extends AppCompatImageView {



    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setChecked(boolean enable){
        if (!enable) {
            setImageResource(R.drawable.ic_preview_radio_on);
            setImageTintList(ColorStateList.valueOf(Color.RED));
        } else {
            setImageResource(R.drawable.ic_preview_radio_off);
            setImageTintList(ColorStateList.valueOf(Color.WHITE));
        }
    }


}
