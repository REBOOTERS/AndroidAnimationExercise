package home.smart.fly.animations.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import home.smart.fly.animations.R;

/**
 * Created by engineer on 2017/9/14.
 */

public class FantasyView extends View {
    private static final int BACKGROUNDCOLOR=0x123456;


    private Paint mPaint;
    private Bitmap mBitmap;

    //
    private int bitmapWidth, bitmapHeight;
    private int centerX, centerY;
    private int drawX, drawY;


    public FantasyView(Context context) {
        super(context);
        init();
    }

    public FantasyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FantasyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        //
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //
        canvas.drawColor(BACKGROUNDCOLOR);

        canvas.drawBitmap(mBitmap, drawX, drawY, mPaint);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmapWidth = mBitmap.getWidth();
        bitmapHeight = mBitmap.getHeight();
        centerX = w / 2;
        centerY = h / 2;
        drawX = centerX - bitmapWidth / 2;
        drawY = centerY - bitmapHeight / 2;
    }
}
