package home.smart.fly.animations.customview.canvas;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by engineer on 2017/7/1.
 */

public class BubbleView extends View {
    private static final String TAG = "BubbleView";
    private Paint mPaint;
    private Path path;

    private PointF centerP;
    private PointF bubbleP;
    //
    private PointF controlP;
    private PointF centerStartP, centerEndP;
    private PointF bubbleStartP, bubbleEndP;

    private int centerRadius;
    private int bubbleRadius;

    private int width, height;

    private int distance = 200;

    private ValueAnimator valueAnimator;

    private AnimatorSet mAnimatorSet;

    private float degree;


    public BubbleView(Context context) {
        super(context);
        init();
    }


    public BubbleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        bubbleRadius = 25;
        centerRadius = bubbleRadius;
        centerP = new PointF(0, 0);
        bubbleP = new PointF(distance, 0);
        path = new Path();

        initPointsValue();

        valueAnimator = ValueAnimator.ofFloat(0, 180);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.e(TAG, "onAnimationUpdate: " + animation.getAnimatedFraction());
                float fraction = animation.getAnimatedFraction();
                if (fraction < 0.2) {
                    fraction = 0.2f;
                }

                if (fraction > 0.8f) {
                    fraction = 0.8f;
                }
                fraction = (fraction < 0.5f) ? (1 - fraction) : fraction;
                degree = (float) animation.getAnimatedValue();
                centerRadius = (int) (bubbleRadius * fraction);
                initPointsValue();
                invalidate();
            }
        });
        valueAnimator.start();
//        valueAnimator = ValueAnimator.ofInt(-200, 200);
//        valueAnimator.setDuration(1000);
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                Log.e(TAG, "onAnimationUpdate: " + animation.getAnimatedFraction());
//                distance = (int) animation.getAnimatedValue();
//                bubbleP.x = distance;
//                float fraction = animation.getAnimatedFraction();
//                if (fraction < 0.2) {
//                    fraction = 0.2f;
//                }
//
//                if (fraction > 0.8f) {
//                    fraction = 0.8f;
//                }
//                fraction = (fraction > 0.5f) ? (1 - fraction) : fraction;
//                centerRadius = (int) (bubbleRadius * fraction * 2);
//
//
//                initPointsValue();
//                invalidate();
//            }
//        });
//        valueAnimator.start();

    }

    private void initPointsValue() {

        controlP = new PointF((centerP.x + bubbleP.x) / 2, (centerP.y + bubbleP.y) / 2);
        //
        float sin = (bubbleP.y - centerP.y) / distance;
        float cos = (bubbleP.x - centerP.x) / distance;

        float centerX = centerRadius * sin;
        float centerY = centerRadius * cos;

        centerStartP = new PointF(centerP.x - centerX, centerP.y + centerY);
        centerEndP = new PointF(centerP.x + centerX, centerP.y - centerY);

        float bubbleX = bubbleRadius * sin;
        float bubbleY = bubbleRadius * cos;

        bubbleStartP = new PointF(bubbleP.x - bubbleX, bubbleP.y - bubbleY);
        bubbleEndP = new PointF(bubbleP.x + bubbleX, bubbleP.y + bubbleY);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        initPointsValue();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        canvas.translate(width / 2, height / 2);
        canvas.rotate(degree);
        path.reset();

        canvas.drawCircle(centerP.x, centerP.y, centerRadius, mPaint);
        canvas.drawCircle(bubbleP.x, bubbleP.y, bubbleRadius, mPaint);


        path.moveTo(centerStartP.x, centerStartP.y);
        path.quadTo(controlP.x, controlP.y, bubbleEndP.x, bubbleEndP.y);
        path.lineTo(bubbleStartP.x, bubbleStartP.y);
        path.quadTo(controlP.x, controlP.y, centerEndP.x, centerEndP.y);
        path.close();
        canvas.drawPath(path, mPaint);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }
}
