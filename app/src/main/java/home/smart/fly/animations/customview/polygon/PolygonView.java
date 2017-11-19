package home.smart.fly.animations.customview.polygon;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.utils.DpConvert;

/**
 * Created by engineer on 2017/11/18.
 */

public class PolygonView extends View {
    private static final String TAG = "PolygonView";

    private Paint mLinePaint;
    private Paint mOverlayPaint;
    private Paint mPointPaint;
    private TextPaint mTextPaint;
    private Paint mLabelPaint;

    private int mOverlayFillColor = Color.argb(110, 0, 0, 255);
    private int mOverlayStrokeColor = Color.argb(190, 0, 0, 255);

    private int screenWidth;

    private int viewWidth;
    private int viewHeight;


    private int labelWidth = 70;
    private int labelHeight = 80;

    private int count = 6;
    private float radius = 400;
    private int levelCount = 5;
    private float angle = (float) (Math.PI * 2 / count);

    private List<List<PointF>> datas = new ArrayList<>();

    private List<PointF> player = new ArrayList<>();
    private List<PointF> label = new ArrayList<>();

    private List<PlayerAbility> mPlayerAbilities = new ArrayList<>();


    public PolygonView(Context context) {
        this(context, null);
    }

    public PolygonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolygonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenWidth = DpConvert.px2dip(context, screenWidth) * 2;

        radius = Math.min(radius, screenWidth);

        float x, y;
        for (int i = 0; i < levelCount; i++) {
            List<PointF> mPointFS = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                float r = radius * (levelCount - i) / levelCount;
                x = (float) (r * Math.cos(j * angle - Math.PI / 2));
                y = (float) (r * Math.sin(j * angle - Math.PI / 2));
                mPointFS.add(new PointF(x, y));
            }
            datas.add(mPointFS);
        }
        //
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStyle(Paint.Style.STROKE);
        //
        mOverlayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(mOverlayStrokeColor);

        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(40);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        //
        mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelPaint.setStyle(Paint.Style.FILL);
        mLabelPaint.setColor(Color.RED);


        mPlayerAbilities = new ArrayList<>();
        mPlayerAbilities.add(new PlayerAbility("shoot", 82));
        mPlayerAbilities.add(new PlayerAbility("speed", 93));
        mPlayerAbilities.add(new PlayerAbility("power", 90));
        mPlayerAbilities.add(new PlayerAbility("defense", 80));
        mPlayerAbilities.add(new PlayerAbility("pandai", 33));
        mPlayerAbilities.add(new PlayerAbility("pass", 90));

        setPlayerDatas();


        ValueAnimator mValueAnimator = ValueAnimator.ofInt(10, 90, 10);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mPlayerAbilities.get(0).setValue(value);
                float r = radius * value / 100;
                float x = (float) (r * Math.cos(0 * angle - Math.PI / 2));
                float y = (float) (r * Math.sin(0 * angle - Math.PI / 2));
                player.get(0).set(new PointF(x, y));
                invalidate();
            }
        });
        mValueAnimator.setDuration(5000);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.start();

    }


    private void setPlayerDatas() {
        for (int i = 0; i < mPlayerAbilities.size(); i++) {
            float r = radius * mPlayerAbilities.get(i).getValue() / 100;
            float x = (float) (r * Math.cos(i * angle - Math.PI / 2));
            float y = (float) (r * Math.sin(i * angle - Math.PI / 2));
            player.add(new PointF(x, y));
        }

        for (int i = 0; i < mPlayerAbilities.size(); i++) {
            float r = radius + 100;
            float x = (float) (r * Math.cos(i * angle - Math.PI / 2));
            float y = (float) (r * Math.sin(i * angle - Math.PI / 2));
            label.add(new PointF(x, y));
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(viewWidth / 2, viewHeight / 2);
        drawPolygonBackground(canvas);
        drawOverlay(canvas);
        drawLaybel(canvas);
    }

    private void drawLaybel(Canvas canvas) {
        canvas.save();

        Path mPath = new Path();

        for (int i = 0; i < count; i++) {
            float x = label.get(i).x;
            float y = label.get(i).y;

            if (i == 0) {
                //如果是每层的第一个点就把path的起点设置为这个点
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }

            if (i % count == 0) {


                RectF mRectF = new RectF(x + 15, 50 + y - labelHeight / 2,
                        x + labelWidth * 2 + 15, 50 + y + labelHeight / 2);
                canvas.drawRoundRect(mRectF, 5, 5, mLabelPaint);
                mTextPaint.setColor(Color.WHITE);
                Paint.FontMetricsInt mFontMetricsInt = mTextPaint.getFontMetricsInt();
                float baseY = (mRectF.top + mRectF.bottom) / 2 -
                        (mFontMetricsInt.top + mFontMetricsInt.bottom) / 2;
                canvas.drawText(String.valueOf(mPlayerAbilities.get(i).getValue()),
                        x + mRectF.width() / 2 + 15, baseY, mTextPaint);
                mTextPaint.setColor(Color.BLACK);
                canvas.drawText(mPlayerAbilities.get(i).getName(), x - labelWidth, baseY, mTextPaint);
            } else if (i % count == count / 2) {
                RectF mRectF = new RectF(x + 15, -50 + y - labelHeight / 2,
                        x + labelWidth * 2 + 15, -50 + y + labelHeight / 2);
                canvas.drawRoundRect(mRectF, 5, 5, mLabelPaint);
                mTextPaint.setColor(Color.WHITE);
                Paint.FontMetricsInt mFontMetricsInt = mTextPaint.getFontMetricsInt();
                float baseY = (mRectF.top + mRectF.bottom) / 2 -
                        (mFontMetricsInt.top + mFontMetricsInt.bottom) / 2;
                canvas.drawText(String.valueOf(mPlayerAbilities.get(i).getValue()),
                        x + mRectF.width() / 2 + 15, baseY, mTextPaint);
                mTextPaint.setColor(Color.BLACK);
                canvas.drawText(mPlayerAbilities.get(i).getName(), x - labelWidth, baseY, mTextPaint);
            } else {
                mTextPaint.setColor(Color.BLACK);
                canvas.drawText(mPlayerAbilities.get(i).getName(), x, y, mTextPaint);
                RectF mRectF = new RectF(x - labelWidth, y + 15,
                        x + labelWidth, y + labelHeight + 15);
                canvas.drawRoundRect(mRectF, 5, 5, mLabelPaint);
                mTextPaint.setColor(Color.WHITE);
                Paint.FontMetricsInt mFontMetricsInt = mTextPaint.getFontMetricsInt();
                float baseY = (mRectF.top + mRectF.bottom) / 2 -
                        (mFontMetricsInt.top + mFontMetricsInt.bottom) / 2;
                canvas.drawText(String.valueOf(mPlayerAbilities.get(i).getValue()),
                        x, baseY, mTextPaint);
            }


        }
        mPath.close();
//        canvas.drawPath(mPath, mLinePaint);
        mPath.reset();
        canvas.restore();
    }

    private void drawOverlay(Canvas canvas) {
        canvas.save();
        Path mPath = new Path();
        float x = 0, y = 0;

        mOverlayPaint.setStyle(Paint.Style.FILL);
        mOverlayPaint.setColor(mOverlayFillColor);
        for (int i = 0; i < count; i++) {
            x = player.get(i).x;
            y = player.get(i).y;
            if (i == 0) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }
            canvas.drawCircle(x, y, 20, mPointPaint);
        }
        mPath.close();
        canvas.drawPath(mPath, mOverlayPaint);

        mOverlayPaint.setStyle(Paint.Style.STROKE);
        mOverlayPaint.setStrokeWidth(8);
        mOverlayPaint.setColor(mOverlayStrokeColor);
        for (int i = 0; i < count; i++) {
            x = player.get(i).x;
            y = player.get(i).y;
            if (i == 0) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }

        }
        mPath.close();

        canvas.drawPath(mPath, mOverlayPaint);


        canvas.restore();
    }


    private void drawPolygonBackground(Canvas canvas) {
        canvas.save();


        Path mPath = new Path();
        for (int i = 0; i < levelCount; i++) {
            for (int j = 0; j < count; j++) {
                float x = datas.get(i).get(j).x;
                float y = datas.get(i).get(j).y;
                if (j == 0) {
                    //如果是每层的第一个点就把path的起点设置为这个点
                    mPath.moveTo(x, y);
                } else {
                    mPath.lineTo(x, y);
                }
            }
            mPath.close();
            canvas.drawPath(mPath, mLinePaint);
            mPath.reset();
        }

        for (int i = 0; i < datas.get(0).size(); i++) {
            canvas.drawLine(0, 0,
                    datas.get(0).get(i).x, datas.get(0).get(i).y, mLinePaint);
        }

        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();

                Log.e(TAG, "onTouchEvent: come here x=" + x);
                Log.e(TAG, "onTouchEvent: come here y=" + y);
                Log.e(TAG, "onTouchEvent: come here playX=" + player.get(0).x);
                Log.e(TAG, "onTouchEvent: come here playY=" + player.get(0).y);


                for (int i = 0; i < count; i++) {
                    if (Math.abs(player.get(i).x - viewWidth / 2 - x) < 0.5
                            || Math.abs(viewHeight / 2 - player.get(i).y - y) < 50) {

                        player.get(i).set(new PointF(viewWidth / 2 - Math.abs(x),
                                viewHeight / 2 - Math.abs(y)));
                        invalidate();
                    }

                }

                return true;
        }
        return super.onTouchEvent(event);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpec == MeasureSpec.AT_MOST) {
            widthSize = screenWidth;
        }
        if (heightSpec == MeasureSpec.AT_MOST) {
            heightSize = screenWidth;
        }


        //保证绘制结果为正方形
        int min = Math.min(widthSize, heightSize);
        setMeasuredDimension(min, min);
    }
}
