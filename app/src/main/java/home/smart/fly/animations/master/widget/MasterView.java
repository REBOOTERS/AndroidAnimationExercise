package home.smart.fly.animations.master.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import home.smart.fly.animations.R;

/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2017/12/10
 * desc   :
 * version: 1.0
 */
public class MasterView extends View {

    private Paint mPaint;
    private int centerX, centerY;
    private int viewW, viewH;

    private RectF mbackRectF;
    private Paint mbackPaint;

    private Resources res;
    private Bitmap source, cover;


    private int mStartColor;
    private int mEndColor;


    private int radius;


    public MasterView(Context context) {
        this(context, null);

    }

    public MasterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MasterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        res = getResources();
        source = BitmapFactory.decodeResource(res, R.drawable.cat);
        cover = BitmapFactory.decodeResource(res, R.drawable.star);
        mStartColor = res.getColor(R.color.cpb_red);
        mEndColor = res.getColor(R.color.cpb_green);

        mPaint.setColor(Color.parseColor("#536DFE"));

        mbackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mbackPaint.setStyle(Paint.Style.STROKE);
        mbackPaint.setStrokeWidth(3);
        mbackPaint.setColor(Color.WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#0097A7"));
        canvas.drawRect(mbackRectF, mbackPaint);
        canvas.drawCircle(centerX, centerY, radius, mPaint);
    }

    public void drawLinearGradient() {
        Shader linearGradient = new LinearGradient(0, (centerY - radius), viewW, (centerY + radius)
                , mStartColor, mEndColor, Shader.TileMode.CLAMP);
        reDraw(linearGradient);
    }

    public void drawRadialsGradient() {
        Shader radialGradient = new RadialGradient(centerX, centerY, radius, mStartColor, mEndColor, Shader.TileMode.CLAMP);
        reDraw(radialGradient);
    }

    public void drawSweepGradient() {
        Shader sweepGradient = new SweepGradient(centerX, centerY, mStartColor, mEndColor);
        reDraw(sweepGradient);
    }

    public void drawBitmapShader() {
        Shader bitmapShader = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        reDraw(bitmapShader);
    }

    public void drawComposeShader(PorterDuff.Mode mode) {
        Shader bitmapShader1 = new BitmapShader(cover, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Shader bitmapShader2 = new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Shader mShader = new ComposeShader(bitmapShader1, bitmapShader2, mode);
        reDraw(mShader);
    }


    private void reDraw(Shader shader) {
        mPaint.setShader(shader);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        radius = w / 3;
        viewW = w;
        viewH = h;

        mbackRectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int min = Math.min(widthSize, heightSize);
        setMeasuredDimension(min, min);
    }
}
