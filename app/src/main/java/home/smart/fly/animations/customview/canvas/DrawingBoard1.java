package home.smart.fly.animations.customview.canvas;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Rookie on 2017/6/29.
 */

public class DrawingBoard1 extends View {
    private static final String TAG = "DrawingBoard";

    public static final float BASE = 200.0f;

    private Context mContext;
    private int screenW, screenH;
    //
    private int widht, height;
    //
    private Paint mPaint;
    private Paint pointPaint;
    private Paint linePaint;

    private PointF startP;
    private PointF endP;


    private PointF controllP1, controllP2;


    private Path path;


    //Animations
    private AnimatorSet mAnimatorSet;

    public DrawingBoard1(Context context) {
        super(context);
        init(context);
    }

    public DrawingBoard1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawingBoard1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;


        Log.e(TAG, "init:UNSPECIFIED= " + MeasureSpec.UNSPECIFIED);
        Log.e(TAG, "init:EXACTLY= " + MeasureSpec.EXACTLY);
        Log.e(TAG, "init:AT_MOST= " + MeasureSpec.AT_MOST);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;

        initPaint();

    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8.0f);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        pointPaint = new Paint();
        pointPaint.setColor(Color.WHITE);
        pointPaint.setStrokeWidth(20.0f);
        pointPaint.setAntiAlias(true);


        linePaint = new Paint();
        linePaint.setColor(Color.DKGRAY);
        linePaint.setStrokeWidth(2.0f);
        linePaint.setAntiAlias(true);


        startP = new PointF(-BASE, 0);
        endP = new PointF(BASE, 0);
        controllP1 = new PointF(-BASE / 2, -BASE);
        controllP2 = new PointF(BASE / 2, BASE);
        path = new Path();


        ValueAnimator mValueAnimator1 = ValueAnimator.ofFloat(-BASE, BASE);
        mValueAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate: value=" + value);
                controllP1.y = value * 0.8f;
                controllP1.x = (float) (BASE * 0.5f * Math.sin(value * Math.PI / BASE));
//                invalidate();
            }
        });
        ValueAnimator mValueAnimator2 = ValueAnimator.ofFloat(BASE, -BASE);
        mValueAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator2.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate: value=" + value);
                controllP2.y = value * 1.2f;
                controllP2.x = (float) (BASE * 0.5f * Math.sin(value * Math.PI / BASE));
                invalidate();
            }
        });

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(mValueAnimator1, mValueAnimator2);
        mAnimatorSet.setDuration(3000);
        mAnimatorSet.start();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        widht = w;
        height = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = Math.min(screenW, screenH);
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = Math.min(screenW, screenH);
        }

        setMeasuredDimension(widthSize, heightSize);

        Log.e(TAG, "onMeasure:widthMode " + widthMode);
        Log.e(TAG, "onMeasure:widthSize " + widthSize);
        Log.e(TAG, "onMeasure:heightMode " + heightMode);
        Log.e(TAG, "onMeasure:heightSize " + heightSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.reset();

        canvas.drawColor(Color.GRAY);
        canvas.translate(widht / 2, height / 2);
        canvas.drawPoint(startP.x, startP.y, pointPaint);
        canvas.drawPoint(endP.x, endP.y, pointPaint);
        canvas.drawPoint(controllP1.x, controllP1.y, pointPaint);
        canvas.drawPoint(controllP2.x, controllP2.y, pointPaint);


        //controll line
        canvas.drawLine(startP.x, startP.y, controllP1.x, controllP1.y, linePaint);
        canvas.drawLine(controllP1.x, controllP1.y, controllP2.x, controllP2.y, linePaint);
        canvas.drawLine(controllP2.x, controllP2.y, endP.x, endP.y, linePaint);


        path.moveTo(startP.x, startP.y);
        path.quadTo(controllP1.x, controllP1.y, endP.x, endP.y);
        canvas.drawPath(path, mPaint);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnimatorSet.cancel();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - widht / 2;
        float y = event.getY() - height / 2;
        Log.e(TAG, "onTouchEvent: x=" + x);
        Log.e(TAG, "onTouchEvent: y=" + y);
        controllP1.x = x;
        controllP1.y = y;
        invalidate();
        return true;


    }
}
