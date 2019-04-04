package home.smart.fly.animations.customview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.animation.AnimationUtils;


/**
 * @author: Rookie
 * @date: 2018-10-17
 * @desc
 */
public class ExpandingCircleAnimationDrawable extends Drawable implements Runnable,Animatable {
    private static final String TAG = "ExpandingCircleAnimatio";

    private Paint mPaint;
    private float mRaaius;
    private long mStartTicks = 0;
    private boolean mIsRunning = false;

    public ExpandingCircleAnimationDrawable(float radius) {
        super();
        mRaaius = radius;
        prepareStuff();
    }

    private void prepareStuff() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        float percent = calculateCurrentPercent();
        float alpha = -(percent * percent)+1;
        float radius = mRaaius * percent;
        mPaint.setAlpha((int) (255 * alpha));
        Rect rect = getBounds();
        float x = (rect.right - rect.left) / 2f + rect.left;
        float y = (rect.bottom - rect.top) /2f + rect.top;
        canvas.drawCircle(x, y, radius, mPaint);
    }

    private float calculateCurrentPercent() {
        float  percent = 0.5f;
        if (isRunning()) {
            long millis = 5000;
            percent = (AnimationUtils.currentAnimationTimeMillis() - mStartTicks) / millis;
            while (percent > 1) {

                percent = percent -1;
                mStartTicks = mStartTicks + millis;
            }
        }

        Log.e(TAG, "calculateCurrentPercent: percent==" + percent);
        return percent;
    }

    @Override
    public void start() {
        if (!isRunning()) {
            mIsRunning = true;
            mStartTicks = AnimationUtils.currentAnimationTimeMillis();
            run();
        }
    }

    @Override
    public void stop() {
        if (isRunning()) {
            unscheduleSelf(this);
            mIsRunning =false;
        }
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    @Override
    public void setAlpha(int alpha) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void run() {
        invalidateSelf();
        scheduleSelf(this, AnimationUtils.currentAnimationTimeMillis() + (1000 / 60));
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (2 * mRaaius);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (2 * mRaaius);
    }

    @Override
    public int getMinimumHeight() {
        return (int) (2 * mRaaius);
    }

    @Override
    public int getMinimumWidth() {
        return (int) (2 * mRaaius);
    }
}
