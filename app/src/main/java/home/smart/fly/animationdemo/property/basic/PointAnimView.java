package home.smart.fly.animationdemo.property.basic;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by co-mall on 2016/8/9.
 */
public class PointAnimView extends View {

    public static final float RADIUS = 20f;

    private Point currentPoint;

    private Paint mPaint;
    private Paint linePaint;

    private AnimatorSet animSet;


    /**
     * 实现关于color 的属性动画
     */
    private String color;

    public PointAnimView(Context context) {
        super(context);
        init();
    }


    public PointAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        mPaint.setColor(Color.parseColor(color));
//        postInvalidate();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (currentPoint == null) {
            currentPoint = new Point(RADIUS, RADIUS);
            drawCircle(canvas);
            StartAnimation();
        } else {
            drawCircle(canvas);
        }

        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, linePaint);
        canvas.drawLine(1,getHeight() / 2 - 150, 1, getHeight() / 2 + 150, linePaint);
        canvas.drawPoint(currentPoint.getX(),currentPoint.getY(),linePaint);

    }

    private void StartAnimation() {
        Point startP = new Point(RADIUS, RADIUS);
        Point endP = new Point(getWidth() - RADIUS, getHeight() - RADIUS);
        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointSinEvaluator(), startP, endP);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(Animation.REVERSE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPoint = (Point) animation.getAnimatedValue();
                postInvalidate();
            }
        });


        ObjectAnimator anim2 = ObjectAnimator.ofObject(this, "color", new ColorEvaluator(),
                "#000000", "#FFFFFF","#00FF00");
        anim2.setRepeatCount(-1);
        anim2.setRepeatMode(Animation.REVERSE);
        animSet = new AnimatorSet();
        animSet.play(valueAnimator).with(anim2);
        animSet.setDuration(5000);
        animSet.start();
    }

    private void drawCircle(Canvas canvas) {
        float x = currentPoint.getX();
        float y = currentPoint.getY();
        canvas.drawCircle(x, y, RADIUS, mPaint);
    }

    public void stopAnimation() {
        if (animSet != null) {
            animSet.cancel();
        }
    }
}
