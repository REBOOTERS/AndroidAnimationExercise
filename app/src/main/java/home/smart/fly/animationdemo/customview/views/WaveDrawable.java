package home.smart.fly.animationdemo.customview.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Choreographer;
import android.view.animation.DecelerateInterpolator;

import static android.content.ContentValues.TAG;

/**
 * Created by jing on 16-12-6.
 */

public class WaveDrawable extends Drawable implements Animatable, ValueAnimator.AnimatorUpdateListener {

    private static final float WAVE_HEIGHT_FACTOR = 0.2f;
    private static final float WAVE_SPEED_FACTOR = 0.02f;
    private static final int UNDEFINED_VALUE = Integer.MIN_VALUE;
    private Drawable mDrawable;
    private int mWidth, mHeight;
    private int mWaveHeight = UNDEFINED_VALUE;
    private int mWaveLength = UNDEFINED_VALUE;
    private int mWaveStep = UNDEFINED_VALUE;
    private int mWaveOffset = 0;
    private int mWaveLevel = 0;
    private ValueAnimator mAnimator = null;
    private float mProgress = 0.3f;
    private Paint mPaint;
    private Bitmap mMask;
    private Matrix mMatrix = new Matrix();
    private boolean mRunning = false;
    private boolean mIndeterminate = false;

    private static final PorterDuffXfermode sXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private static ColorFilter sGrayFilter = new ColorMatrixColorFilter(new float[]{
            0.264F, 0.472F, 0.088F, 0, 0,
            0.264F, 0.472F, 0.088F, 0, 0,
            0.264F, 0.472F, 0.088F, 0, 0,
            0,      0,      0,      1, 0
    });
    private ColorFilter mCurFilter = null;

    private Choreographer.FrameCallback mFrameCallback = new Choreographer.FrameCallback() {
        @Override
        public void doFrame(long l) {
            invalidateSelf();
            if (mRunning) {
                Choreographer.getInstance().postFrameCallback(this);
            }
        }
    };

    public WaveDrawable(Drawable drawable) {
        init(drawable);
    }

    public WaveDrawable(Context context, int imgRes) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(imgRes);
        } else {
            drawable = context.getResources().getDrawable(imgRes);
        }

        init(drawable);
    }

    private void init(Drawable drawable) {
        mDrawable = drawable;
        mMatrix.reset();
        mPaint = new Paint();
        mPaint.setFilterBitmap(false);
        mPaint.setColor(Color.BLACK);
        mPaint.setXfermode(sXfermode);

        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();

        if (mWidth > 0 && mHeight > 0) {
            mWaveLength = mWidth;
            mWaveHeight = Math.max(8, (int) (mHeight * WAVE_HEIGHT_FACTOR));
            mWaveStep = Math.max(1, (int) (mWidth * WAVE_SPEED_FACTOR));
            updateMask(mWidth, mWaveLength, mWaveHeight);
        }

        setProgress(0);
        start();
    }

    /**
     * Set wave move distance (in pixels) in very animation frame
     * @param step distance in pixels
     */
    public void setWaveSpeed(int step) {
        mWaveStep = Math.min(step, mWidth / 2);
    }

    /**
     * Set wave amplitude (in pixels)
     * @param amplitude
     */
    public void setWaveAmplitude(int amplitude) {
        amplitude = Math.max(1, Math.min(amplitude, mHeight / 2));
        int height = amplitude * 2;
        if (mWaveHeight != height) {
            mWaveHeight = height;
            updateMask(mWidth, mWaveLength, mWaveHeight);
            invalidateSelf();
        }
    }

    /**
     * Set wave length (in pixels)
     * @param length
     */
    public void setWaveLength(int length) {
        length = Math.max(8, Math.min(mWidth * 2, length));
        if (length != mWaveLength) {
            mWaveLength = length;
            updateMask(mWidth, mWaveLength, mWaveHeight);
            invalidateSelf();
        }
    }

    /**
     * Set the wave loading in indeterminate mode or not
     * @param indeterminate
     */
    public void setIndeterminate(boolean indeterminate) {
        mIndeterminate = indeterminate;
        if (mIndeterminate) {
            if (mAnimator == null) {
                mAnimator = getDefaultAnimator();
            }
            mAnimator.addUpdateListener(this);
            mAnimator.start();
        } else {
            if (mAnimator != null) {
                mAnimator.removeUpdateListener(this);
                mAnimator.cancel();
            }
            setLevel(calculateLevel());
        }
    }

    /**
     * Set customised animator for wave loading animation
     * @param animator
     */
    public void setIndeterminateAnimator(ValueAnimator animator) {
        if (mAnimator == animator) {
            return;
        }

        if (mAnimator != null) {
            mAnimator.removeUpdateListener(this);
            mAnimator.cancel();
        }

        mAnimator = animator;
        if (mAnimator != null) {
            mAnimator.addUpdateListener(this);
        }
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mDrawable.setBounds(left, top, right, bottom);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updateBounds(bounds);
    }

    private void updateBounds(Rect bounds) {
        if (bounds.width() <= 0 || bounds.height() <= 0) {
            return;
        }

        if (mWidth < 0 || mHeight < 0) {
            mWidth = bounds.width();
            mHeight = bounds.height();
            if (mWaveHeight == UNDEFINED_VALUE) {
                mWaveHeight = Math.max(8, (int) (mHeight * WAVE_HEIGHT_FACTOR));
            }

            if (mWaveLength == UNDEFINED_VALUE) {
                mWaveLength = mWidth;
            }

            if (mWaveStep == UNDEFINED_VALUE) {
                mWaveStep = Math.max(1, (int) (mWidth * WAVE_SPEED_FACTOR));
            }

            updateMask(mWidth, mWaveLength, mWaveHeight);
        }
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public void draw(Canvas canvas) {

        mDrawable.setColorFilter(sGrayFilter);
        mDrawable.draw(canvas);
        mDrawable.setColorFilter(mCurFilter);

        if (mProgress <= 0.001f) {
            return;
        }

        int sc = canvas.saveLayer(0, 0, mWidth, mHeight, null,
                Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        if (mWaveLevel > 0) {
            canvas.clipRect(0, mWaveLevel, mWidth, mHeight);
        }

        mDrawable.draw(canvas);

        if (mProgress >= 0.999f) {
            return;
        }

        mWaveOffset += mWaveStep;
        if (mWaveOffset > mWaveLength) {
            mWaveOffset -= mWaveLength;
        }

        if (mMask != null) {
            mMatrix.setTranslate(-mWaveOffset, mWaveLevel);
            canvas.drawBitmap(mMask, mMatrix, mPaint);
        }

        canvas.restoreToCount(sc);
    }

    @Override
    protected boolean onLevelChange(int level) {
        setProgress(level / 10000f);
        return true;
    }

    @Override
    public void setAlpha(int i) {
        mDrawable.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mCurFilter = colorFilter;
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start() {
        mRunning = true;
        Choreographer.getInstance().postFrameCallback(mFrameCallback);
    }

    @Override
    public void stop() {
        mRunning = false;
        Choreographer.getInstance().removeFrameCallback(mFrameCallback);
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (mIndeterminate) {
            setProgress(animation.getAnimatedFraction());
            if (!mRunning) {
                invalidateSelf();
            }
        }
    }

    public boolean isIndeterminate() {
        return mIndeterminate;
    }

    private ValueAnimator getDefaultAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(5000);
        return animator;
    }

    private void setProgress(float progress) {
        mProgress = progress;
        mWaveLevel = mHeight - (int)((mHeight + mWaveHeight) * mProgress);
        invalidateSelf();
    }

    private int calculateLevel() {
        return (mHeight - mWaveLevel) * 10000 / (mHeight + mWaveHeight);
    }

    private void updateMask(int width, int length, int height) {
        if (width <= 0 || length <= 0 || height <= 0) {
            Log.w(TAG, "updateMask: size must > 0");
            mMask = null;
            return;
        }


        final int count = (int) Math.ceil((width + length) / (float)length);

        Bitmap bm = Bitmap.createBitmap(length * count, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        int amplitude = height / 2;
        Path path = new Path();
        path.moveTo(0, amplitude);

        final float stepX = length / 4f;
        float x = 0;
        float y = -amplitude;
        for (int i = 0; i < count * 2; i++) {
            x += stepX;
            path.quadTo(x, y, x+stepX, amplitude);
            x += stepX;
            y = bm.getHeight() - y;
        }
        path.lineTo(bm.getWidth(), height);
        path.lineTo(0, height);
        path.close();

        c.drawPath(path, p);

        mMask = bm;
    }
}
