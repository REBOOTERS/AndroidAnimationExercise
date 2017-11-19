package home.smart.fly.animations.customview.polygon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import home.smart.fly.animations.utils.DpConvert;

/**
 * Created by engineer on 2017/11/18.
 */

public class PolygonView extends View {

    private Paint mLinePaint;
    private TextPaint mTextPaint;

    private int screenWidth;

    private int viewWidht;
    private int viewHeight;

    private int count = 7;
    private float radius = 400;
    private int levelCount = 4;
    private float angle = (float) (Math.PI * 2 / count);

    private List<List<PointF>> datas = new ArrayList<>();


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

        float x;
        float y;
        for (int i = 0; i < levelCount; i++) {
            List<PointF> mPointFS = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                float r = radius * (4 - i) / levelCount;
                x = (float) (r * Math.cos(j * angle - Math.PI / 2));
                y = (float) (r * Math.sin(j * angle - Math.PI / 2));
                mPointFS.add(new PointF(x, y));
            }
            datas.add(mPointFS);
        }
        //
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new TextPaint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(viewWidht / 2, viewHeight / 2);
        drawPolygon(canvas);
    }


    private void drawPolygon(Canvas canvas) {
        canvas.save();
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);  //设置为填充且描边

        Path path = new Path();  //路径
        for (int i = 0; i < levelCount; i++) {
            switch (i) {
                case 0:
                    mLinePaint.setColor(Color.parseColor("#D4F0F3"));
                    break;
                case 1:
                    mLinePaint.setColor(Color.parseColor("#99DCE2"));
                    break;
                case 2:
                    mLinePaint.setColor(Color.parseColor("#56C1C7"));
                    break;
                case 3:
                    mLinePaint.setColor(Color.parseColor("#278891"));
                    break;
            }
            for (int j = 0; j < count; j++) {
                float x = datas.get(i).get(j).x;
                float y = datas.get(i).get(j).y;
                if (j == 0) {
                    //如果是每层的第一个点就把path的起点设置为这个点
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
            }
            path.close();  //设置为闭合的
            canvas.drawPath(path, mLinePaint);
            path.reset();   //清除path存储的路径
        }

        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidht = w;
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
