package home.smart.fly.animationdemo.customview.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import home.smart.fly.animationdemo.R;
import home.smart.fly.animationdemo.customview.views.model.PointObj;


/**
 * TODO: document your custom view class.
 */
public class SpinnerLoading extends View {
    private int viewWidth = 150;
    private int viewHeight = 150;

    private PointObj[] points;

    private int pointColor = Color.WHITE;
    private int pointCount = 8;
    private int rotateDegree = 360 / pointCount;
    private int speed = 120;

    private PointF center;


    public SpinnerLoading(Context context) {
        super(context);
        init(null, 0);
    }

    public SpinnerLoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SpinnerLoading(Context context, AttributeSet attrs, int defStyle) {
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
        points = new PointObj[pointCount];
        for (int i = 0; i < pointCount; i++) {
            points[i] = new PointObj();
            points[i].setColor(pointColor);
            points[i].setPointRadius(pointRadius);
            points[i].setCenter(center.x, pointRadius);

            //
            final int pos = i;
            ValueAnimator fadeAnimator = ValueAnimator.ofInt(126, 256, 126);
            fadeAnimator.setStartDelay(speed * pos);
            fadeAnimator.setDuration(speed * pointCount);
            fadeAnimator.setRepeatCount(ValueAnimator.INFINITE);
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    points[pos].setAlpha((Integer) animation.getAnimatedValue());
                    invalidate();
                }
            });
            fadeAnimator.start();
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < pointCount; i++) {
            canvas.save();
            canvas.rotate(i * rotateDegree, center.x, center.y);
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
