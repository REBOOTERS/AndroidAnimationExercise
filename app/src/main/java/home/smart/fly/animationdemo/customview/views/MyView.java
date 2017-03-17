package home.smart.fly.animationdemo.customview.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by rookie on 2016/12/22.
 */

public class MyView extends View {

    private static final String TAG = "MyView";

    private Context mContext;

    private static final int DEFAULT_VALUE = 300;
    private static final int DEFAULT_PADDING = 60;

    private int pivotX = DEFAULT_VALUE;
    private int pivotY = DEFAULT_VALUE;
    private int radius = DEFAULT_VALUE;

    private int viewWidth;
    private int padding = DEFAULT_PADDING;


    private Paint paint;


    public MyView(Context context) {
        super(context, null);
        init(context);
    }


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        //
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);

        final int MODE_SHIFT = 30;
        final int MODE_MASK = 0x3 << MODE_SHIFT;

        final int UNSPECIFIED = 0 << MODE_SHIFT;

        final int EXACTLY = 1 << MODE_SHIFT;

        final int AT_MOST = 2 << MODE_SHIFT;

        Log.e(TAG, "loadData: MODE_SHIFT " + MODE_SHIFT);
        Log.e(TAG, "loadData: MODE_MASK " + MODE_MASK);
        Log.e(TAG, "loadData: UNSPECIFIED " + UNSPECIFIED);
        Log.e(TAG, "loadData: EXACTLY " + EXACTLY);
        Log.e(TAG, "loadData: AT_MOST " + AT_MOST);

        int screenW = new WindowHelper(mContext).getScreenW();
        int screenH = new WindowHelper(mContext).getScreenH();

        viewWidth = Math.min(screenW, screenH);

        pivotX = viewWidth / 2;
        pivotY = viewWidth / 2;
        radius = viewWidth / 2 - padding;


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged: "
                + "\n w: " + w
                + "\n h: " + h
                + "\n oldw: " + oldw
                + "\n oldh: " + oldh);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        Log.e(TAG, "onMeasure: widthMode " + widthMode);
        Log.e(TAG, "onMeasure: widhtSize " + widthSize);
        Log.e(TAG, "onMeasure: heightMode " + heightMode);
        Log.e(TAG, "onMeasure: heightSize " + heightSize);
        Log.e(TAG, "------------------------------------");

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(viewWidth, viewWidth);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(viewWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, viewWidth);
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.LTGRAY);
        canvas.drawCircle(pivotX, pivotY, radius, paint);
    }
}
