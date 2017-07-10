package home.smart.fly.animations.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import home.smart.fly.animations.R;
import home.smart.fly.animations.utils.Tools;

/**
 * Created by engineer on 2017/7/10.
 */

public class DragView extends View {
    private static final String TAG = "DragView";
    private int screenWidth, screenHeight;
    private int viewWidht, viewHeight;

    private Paint mPaint;

    private Bitmap mBitmap;

    private Rect mRect;
    private Rect destRect;

    public DragView(Context context) {
        super(context);
        init(context);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context mContext) {
        screenWidth = Tools.getScreenWidth(mContext);
        screenHeight = Tools.getScreenHeight(mContext);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.a6);

        viewWidht = mBitmap.getWidth();
        viewHeight = mBitmap.getHeight();

        mRect = new Rect(0, 0, viewWidht, viewHeight);
        destRect = new Rect(0, screenHeight / 2 - viewHeight / 2, screenWidth, screenHeight / 2 + viewHeight / 2);

        Log.e(TAG, "init: width= " + mBitmap.getWidth());
        Log.e(TAG, "init: height= " + mBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mRect, destRect, mPaint);
    }

}
