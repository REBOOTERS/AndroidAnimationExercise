package com.engineer.imitate.widget.imageviews;

import android.content.Context;
import android.graphics.Canvas;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * @author: zhuyongging
 * @since: 2019-03-17
 */
public class CustomImageView extends AppCompatImageView {

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
