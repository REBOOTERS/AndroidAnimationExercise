package home.smart.fly.animations.master.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import home.smart.fly.animations.R;

/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2017/12/17
 * desc   :
 * version: 1.0
 */
public class MasterFilterView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private Rect mBitmapRect;
    private RectF mDestRectF;

    private ColorFilter mColorFilter;

    public MasterFilterView(Context context) {
        this(context, null);
    }

    public MasterFilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MasterFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.miui_nine);
        mBitmapRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
    }

    public void setFilterAndUpdate(ColorFilter filter) {
        mPaint.setColorFilter(filter);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#455A64"));
        canvas.drawBitmap(mBitmap,mBitmapRect,mDestRectF,mPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int min = Math.min(getMeasuredHeight(), getMeasuredWidth());
        setMeasuredDimension(min, min);
        mDestRectF = new RectF(0, 0, min, min);
    }
}
