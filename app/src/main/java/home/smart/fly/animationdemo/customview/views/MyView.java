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

    private Paint paint;


    public MyView(Context context) {
        super(context);
        init();
    }


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);

        final int MODE_SHIFT = 30;
        final int MODE_MASK = 0x3 << MODE_SHIFT;

        final int UNSPECIFIED = 0 << MODE_SHIFT;

        final int EXACTLY = 1 << MODE_SHIFT;

        final int AT_MOST = 2 << MODE_SHIFT;

        Log.e(TAG, "init: MODE_SHIFT " + MODE_SHIFT);
        Log.e(TAG, "init: MODE_MASK " + MODE_MASK);
        Log.e(TAG, "init: UNSPECIFIED " + UNSPECIFIED);
        Log.e(TAG, "init: EXACTLY " + EXACTLY);
        Log.e(TAG, "init: AT_MOST " + AT_MOST);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widhtSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        Log.e(TAG, "onMeasure: widthMode " + widthMode);
        Log.e(TAG, "onMeasure: widhtSize " + widhtSize);
        Log.e(TAG, "onMeasure: heightMode " + heightMode);
        Log.e(TAG, "onMeasure: heightSize " + heightSize);
        Log.e(TAG, "------------------------------------");


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(100, 100, 150, paint);
    }
}
