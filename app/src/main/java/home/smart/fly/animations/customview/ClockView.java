package home.smart.fly.animations.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;


/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2018/1/14
 * desc   :
 * version: 1.0
 */
public class ClockView extends View {
    private static final String TAG = "ClockView";
    // 默认一分钟
    private final int DEFAULT_TIME = 60;
    // 最大值 一小时
    private final int MAX_TIME = 60 * 60;

    private int factor=100;

    private Paint mCirclePaint;
    private Paint mArcPaint;
    private TextPaint mTextPaint;
    private int BLACK = Color.BLACK;
    private int WHITE = Color.WHITE;
    private int RED = Color.RED;

    private int centerX, centerY;
    private int radius;

    private RectF mRectF;

    //

    private float degree = 0;
    private int counter = 0;
    /**
     * 时长 秒
     */
    private int time = DEFAULT_TIME;
    private ValueAnimator mAnimator;

    public ClockView(Context context) {
        super(context);
        initPaint(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context) {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(BLACK);
        mCirclePaint.setStrokeWidth(12);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setColor(RED);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(12);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(RED);
        mTextPaint.setTextSize(80);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        initAnim();

    }

    private void initAnim() {
        mAnimator = ValueAnimator.ofFloat(360);
        mAnimator.setDuration(time * 1000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e(TAG, "onAnimationUpdate: fraction=" + animation.getAnimatedFraction() * time);
                Log.e(TAG, "onAnimationUpdate: value=" + animation.getAnimatedValue());
                counter = (int) ((1 - animation.getAnimatedFraction()) * time);
                degree = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    public void setTime(int time) {
        if (time > MAX_TIME) {
            time = MAX_TIME;
        }
        this.time = time;
        counter = time;
        initAnim();
        invalidate();
    }


    public void start() {
        if (mAnimator != null) {
            mAnimator.start();
        }
    }

    public void pause() {
        if (mAnimator != null) {
            if (mAnimator.isRunning()) {
                mAnimator.pause();
            } else if (mAnimator.isPaused()) {
                mAnimator.resume();
            }
        }
    }


    public void stop() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(WHITE);
        canvas.drawCircle(centerX, centerY, radius, mCirclePaint);
//        canvas.drawRect(mRectF,mArcPaint);
        canvas.drawText(formatCountdownTime(counter), centerX, centerY, mTextPaint);
        canvas.drawArc(mRectF, -90, degree, false, mArcPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
        // 规定视图大小为和屏幕同宽的正方形
        setMeasuredDimension(min, min);
        centerX = min / 2;
        centerY = min / 2;
        radius = min / 3;
        Log.e(TAG, "onMeasure: radius=" + radius);

    }

    float touchX, touchY;
    int lastCounter;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:


                touchX = event.getX();
                touchY = event.getY();


                lastCounter = counter;

                if (mAnimator != null && mAnimator.isRunning()) {
                    mAnimator.cancel();
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int offsetY = (int) (touchY - y);
                int offsetX = (int) (touchX - x);
                if (Math.abs(offsetY) > Math.abs(offsetX) && !isOutOfBounds(x, y)) {
                    float percent = (offsetY * factor / radius);
                    Log.e(TAG, "onTouchEvent: percent==" + percent);
                    Log.e(TAG, "onTouchEvent: offset==" + offsetY);
                    time = (int) (percent + lastCounter);

                    if (time < 0) {
                        time = 0;
                    }
                    setTime(time);
                }

                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    /**
     * 判断滑动边界是否超出
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isOutOfBounds(float x, float y) {
        return Math.sqrt((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y)) - radius > 0;
    }

    private String formatCountdownTime(int countdownTime) {
        StringBuilder sb = new StringBuilder();
        int minute = countdownTime / 60;
        int second = countdownTime - 60 * minute;
        if (minute < 10) {
            sb.append("0" + minute + ":");
        } else {
            sb.append(minute + ":");
        }
        if (second < 10) {
            sb.append("0" + second);
        } else {
            sb.append(second);
        }
        return sb.toString();
    }
}
