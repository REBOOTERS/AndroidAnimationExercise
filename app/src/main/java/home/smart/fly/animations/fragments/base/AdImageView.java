package home.smart.fly.animations.fragments.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * @author: Rookie
 * @date: 2018-10-16
 * @desc
 */
public class AdImageView extends AppCompatImageView {


    private int mDx;
    private int mMinDx;
    private Drawable mDrawable;

    private int w,h;

    //<editor-fold desc="Construct">
    public AdImageView(Context context) {
        super(context);
        initDraw();
    }

    public AdImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDraw();
    }

    public AdImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDraw();
    }
    //</editor-fold>

    private void initDraw() {
//        getDrawableInternal();
//        int w = getWidth();
//        int h = (int) (getWidth() * 1.0f / mDrawable.getIntrinsicWidth() * mDrawable.getIntrinsicHeight());
//        mDrawable.setBounds(0, 0, w, h);
    }

    private Drawable getDrawableInternal() {
        if (mDrawable == null) {
            mDrawable = getDrawable();
        }
        return mDrawable;
    }

    public void setDx(int dx) {
        if (getDrawable() == null) {
            return;
        }
        mDx = dx - mMinDx;
        if (mDx <= 0) {
            mDx = 0;
        }
        if (mDx > getDrawable().getBounds().height() - mMinDx) {
            mDx = getDrawable().getBounds().height() - mMinDx;
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMinDx = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        int w = getWidth();
        int h = (int) (getWidth() * 1.0f / drawable.getIntrinsicWidth() * drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, w, h);
        canvas.save();
        canvas.translate(0, -mDx);
        super.onDraw(canvas);
        canvas.restore();
    }

}
