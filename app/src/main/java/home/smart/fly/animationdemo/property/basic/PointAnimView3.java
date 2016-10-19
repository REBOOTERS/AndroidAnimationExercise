package home.smart.fly.animationdemo.property.basic;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by co-mall on 2016/8/9.
 */
public class PointAnimView3 extends View {

    public static final float RADIUS = 50f;

    private Point currentPoint;

    private Paint mPaint;

    private ValueAnimator valueAnimator;


    public PointAnimView3(Context context) {
        super(context);
        init();
    }


    public PointAnimView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointAnimView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentPoint == null) {
            currentPoint = new Point(getWidth()/2, RADIUS);
            drawCircle(canvas);
            StartAnimation();
        } else {
            drawCircle(canvas);
        }

    }


    private void StartAnimation() {
        Point startP = new Point(getWidth() / 2, RADIUS);
        Point endP = new Point(getWidth() / 2, getHeight() - RADIUS);
        valueAnimator = ValueAnimator.ofObject(new PointEvaluator(), startP, endP);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        LinearInterpolator l;
//        valueAnimator.setInterpolator(new AccelerateInterpolator(2f));
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.setDuration(5000);
        valueAnimator.start();


    }

    private void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x, y, RADIUS, mPaint);
    }

    public void stopAnimation() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }
}
