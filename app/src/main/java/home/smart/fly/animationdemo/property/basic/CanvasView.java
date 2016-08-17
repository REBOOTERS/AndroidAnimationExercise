package home.smart.fly.animationdemo.property.basic;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by co-mall on 2016/8/12.
 */
public class CanvasView extends View {

    private Paint mTextPaint;//TextView
    private Paint mImagePaint1;//ImageView
    private Paint mImagePaint2;//ImageView
    private Paint mPointPaint;//Point;
    private Path mPath2;
    private Point point21, point22, point23;
    //
    private Path mPath3;
    private Point point31, point32, point33, point34;

    //
    private Point animPoint;
    private ValueAnimator valueAnimator;

    Point p1;
    Point p2;

    public CanvasView(Context context) {
        super(context);
        InitPaint();
    }


    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitPaint();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitPaint();
    }


    private void InitPaint() {
        mTextPaint = new Paint();
        mTextPaint.setTextScaleX(1);
        mTextPaint.setTextSize(28);
        mTextPaint.setColor(Color.BLACK);
        //
        mImagePaint1 = new Paint();
        mImagePaint1.setAntiAlias(true);
        mImagePaint1.setColor(Color.GREEN);
        mImagePaint1.setStrokeWidth(8);
        //
        mImagePaint2 = new Paint();
        mImagePaint2.setAntiAlias(true);
        mImagePaint2.setColor(Color.RED);
        mImagePaint2.setStrokeWidth(8);
        mImagePaint2.setStyle(Paint.Style.STROKE);
        ///
        mPointPaint = new Paint();
        mPointPaint.setColor(Color.GRAY);
        mPointPaint.setStrokeWidth(15);
        //
        mPath2 = new Path();
        point21 = new Point(100, 400);
        point22 = new Point(440, 200);
        point23 = new Point(700, 400);
        mPath3 = new Path();
        point31 = new Point(100, 900);
        point32 = new Point(450, 1110);
        point33 = new Point(580, 980);
        point34 = new Point(700, 900);

        p1 = new Point(100, 700);
        p2 = new Point(1080 - 100, 1860 - 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
//        canvas.drawText("一阶贝塞尔曲线：", 100, 50, mTextPaint);
//        canvas.drawLine(100, 100, 500, 100, mImagePaint1);
//        canvas.drawText("二阶贝塞尔曲线：", 100, 150, mTextPaint);
        //
//        mPath2.moveTo(point21.x, point21.y);
//        mPath2.quadTo(point22.x, point22.y, point23.x, point23.y);
//        canvas.drawPath(mPath2, mImagePaint2);
//        canvas.drawPoint(point21.x, point21.y, mPointPaint);
//        canvas.drawPoint(point22.x, point22.y, mPointPaint);
//        canvas.drawPoint(point23.x, point23.y, mPointPaint);


        if (animPoint != null) {
            canvas.drawCircle(animPoint.x, animPoint.y, 10, mImagePaint2);
        }


        //
//        canvas.drawText("三阶贝塞尔曲线：", 100, 850, mTextPaint);
//        mPath3.moveTo(point31.x, point31.y);
//        mPath3.cubicTo(point32.x, point32.y, point33.x, point33.y, point34.x, point34.y);
//        canvas.drawPath(mPath3, mImagePaint2);
//        canvas.drawPoint(point31.x, point31.y, mPointPaint);
//        canvas.drawPoint(point32.x, point32.y, mPointPaint);
//        canvas.drawPoint(point33.x, point33.y, mPointPaint);
//        canvas.drawPoint(point34.x, point34.y, mPointPaint);


        canvas.drawPoint(p1.x, p1.y, mPointPaint);
        canvas.drawPoint(p2.x, p2.y, mPointPaint);
        canvas.drawPoint(Math.abs(p1.x - p2.x) * 2 / 3, 100, mPointPaint);

        mPath2.moveTo(100, 200);
        mPath2.quadTo(Math.abs(100 - (getWidth() - 100)) * 2 / 3, 100, getWidth() - 100, getHeight() - 100);
        canvas.drawPath(mPath2, mImagePaint2);

        Log.e("x", "--------" + getWidth());
        Log.e("y", "--------" + getHeight());
    }

    public void startAnim() {


        valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(), p1, p2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animPoint = (Point) animation.getAnimatedValue();
                Log.e("x", "------" + animPoint.x);
                Log.e("y", "------" + animPoint.y);
                postInvalidate();
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.start();
    }

    public void stopAnim() {
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
        }
    }
}
