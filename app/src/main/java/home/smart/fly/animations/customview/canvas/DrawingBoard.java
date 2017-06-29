package home.smart.fly.animations.customview.canvas;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Rookie on 2017/6/29.
 */

public class DrawingBoard extends View {
    private static final String TAG = "DrawingBoard";

    private static final int SCALE = 10;

    private Context mContext;
    private int screenW, screenH;
    //
    private int widht, height;
    //
    private Paint mPaint;
    private Paint pointPaint;
    private Paint linePaint;

    private PointF startP, endP, controllP;


    private Path path;


    public DrawingBoard(Context context) {
        super(context);
        init(context);
    }

    public DrawingBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawingBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        int MODE_SHIFT = 30;
        int MODE_MASK = 0x3 << MODE_SHIFT;
        int UNSPECIFIED = 0 << MODE_SHIFT;
        int EXACTLY = 1 << MODE_SHIFT;
        int AT_MOST = 2 << MODE_SHIFT;

        Log.e(TAG, "init:UNSPECIFIED= " + UNSPECIFIED);
        Log.e(TAG, "init:EXACTLY= " + EXACTLY);
        Log.e(TAG, "init:AT_MOST= " + AT_MOST);

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
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(2.0f);
        linePaint.setAntiAlias(true);


        startP = new PointF(-300, 0);
        endP = new PointF(300, 0);
        controllP = new PointF(0, -300);


        path = new Path();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int i = -300;
//                while (i < 300) {
//                    try {
//                        Thread.sleep(100);
//                        i = i + 10;
//                        controllP.y = i;
//                        postInvalidate();
//                        Log.e(TAG, "run: " + i);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//
//                    }
//                }
//
//            }
//        }).start();

        int a = 0;
        int wxb = 1;
        while (true) {


            a = a + wxb;


            if (a == 10 || a == 0) {
                wxb = -wxb;
            }


            System.out.println("--------a=" + a);

        }


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

        canvas.drawColor(Color.BLACK);
        canvas.translate(widht / 2, height / 2);
        canvas.drawPoint(startP.x, startP.y, pointPaint);
        canvas.drawPoint(endP.x, endP.y, pointPaint);
        canvas.drawPoint(controllP.x, controllP.y, pointPaint);


        //controll line
        canvas.drawLine(startP.x, startP.y, controllP.x, controllP.y, linePaint);
        canvas.drawLine(endP.x, endP.y, controllP.x, controllP.y, linePaint);


        path.moveTo(startP.x, startP.y);
        path.quadTo(controllP.x, controllP.y, endP.x, endP.y);
        canvas.drawPath(path, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - widht / 2;
        float y = event.getY() - height / 2;


        Log.e(TAG, "onTouchEvent: x=" + x);
        Log.e(TAG, "onTouchEvent: y=" + y);
        controllP.x = x;
        controllP.y = y;

        invalidate();


        return true;


    }
}
