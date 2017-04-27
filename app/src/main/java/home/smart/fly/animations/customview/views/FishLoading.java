package home.smart.fly.animations.customview.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import home.smart.fly.animations.R;
import home.smart.fly.animations.customview.views.model.PointObj;


/**
 * TODO: document your custom view class.
 */
public class FishLoading extends View {
    private int viewWidth = 150;
    private int viewHeight = 150;

    private PointObj[] points;
    private float[] rotates;

    private int pointColor = Color.WHITE;
    private int pointCount = 5;
    private int speed = 160;

    private PointF center;

    private int style = 1;


    public FishLoading(Context context) {
        super(context);
        init(null, 0);
    }

    public FishLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FishLoading(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.SpinnerLoading, defStyle, 0);
        a.recycle();

        initAnimObj();
    }

    private void initAnimObj() {
        //point
        center = new PointF(viewWidth / 2, viewHeight / 2);
        int size = Math.min(viewWidth, viewHeight);
        int pointRadius = size / 10;

        if (style == 0) {
            points = new PointObj[pointCount];
            rotates = new float[pointCount];
            for (int i = 0; i < pointCount; i++) {
                points[i] = new PointObj();
                points[i].setColor(pointColor);
                points[i].setCenter(center.x, pointRadius);
                points[i].setPointRadius(pointRadius - pointRadius * i / 6);

                //
                final int pos = i;
                ValueAnimator fadeAnimator = ValueAnimator.ofFloat(0, 180);
                fadeAnimator.setStartDelay(100 * pos);
                fadeAnimator.setDuration(1700);
                fadeAnimator.setRepeatCount(ValueAnimator.INFINITE);
                fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        rotates[pos] = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                fadeAnimator.start();
            }
        } else {
            pointCount = pointCount * 2;
            points = new PointObj[pointCount];
            rotates = new float[pointCount];
            for (int i = 0; i < pointCount / 2; i++) {
                points[i] = new PointObj();
                points[i].setColor(pointColor);
                points[i].setCenter(center.x, pointRadius);
                points[i].setPointRadius(pointRadius - pointRadius * i / 6);
            }

            for (int i = pointCount / 2; i < pointCount; i++) {
                points[i] = new PointObj();
                points[i].setColor(pointColor);
                points[i].setCenter(center.x, size - pointRadius);
                points[i].setPointRadius(pointRadius - pointRadius * (i - pointCount / 2) / 6);
            }

            for (int i = 0; i < pointCount; i++) {
                //
                final int pos = i;
                ValueAnimator fadeAnimator = ValueAnimator.ofFloat(0, 360);
                fadeAnimator.setStartDelay(100 * (pos >= pointCount / 2 ? pos - pointCount / 2 : pos));
                fadeAnimator.setDuration(1700);
                fadeAnimator.setRepeatCount(ValueAnimator.INFINITE);
                fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        rotates[pos] = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                fadeAnimator.start();
            }
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < pointCount; i++) {
            canvas.save();
            canvas.rotate(rotates[i], center.x, center.y);
            points[i].draw(canvas);
            canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int measuredWidth = resolveSize(viewWidth, widthMeasureSpec);
        final int measuredHeight = resolveSize(viewHeight, heightMeasureSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}
