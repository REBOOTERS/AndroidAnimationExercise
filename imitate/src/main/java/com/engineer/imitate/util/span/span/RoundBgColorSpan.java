package com.engineer.imitate.util.span.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import android.text.style.ReplacementSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * 参阅：
 * https://blog.csdn.net/aprilqq/article/details/52244308
 * https://www.cnblogs.com/withwind318/p/5541267.html
 * https://www.cnblogs.com/tianzhijiexian/p/4297664.html
 * <p>
 * Create by liyujiang on 2018/12/27 18:20
 */
public class RoundBgColorSpan extends ReplacementSpan {
    private int spanSize;
    private int bgColor;
    private int textColor;
    private int radiusPx;
    private int paddingPx;

    public RoundBgColorSpan(@ColorInt int bgColor, @ColorInt int textColor, int radiusPx) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.radiusPx = radiusPx;
        this.paddingPx = 10;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        spanSize = Math.round(paint.measureText(text, start, end) + 2 * paddingPx);
        return spanSize;
    }

    /**
     * x，要绘制的span的左边框到textview左边框的距离。
     * y，要替换的文字的基线坐标，即基线到textview上边框的距离。
     * top，替换行的最顶部位置。
     * bottom，替换行的最底部位置，注意，textview中两行之间的行间距是属于上一行的，所以这里bottom是指行间隔的底部位置。
     * paint，画笔，包含了要绘制字体的度量信息。
     * y + fm.ascent得到字体的ascent线坐标，y + fm.descent得到字体的descent线坐标，两者相加除以2就是两条线中线的坐标；
     */
    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        paint.setColor(bgColor);
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        RectF rectF = new RectF(x, y + fm.ascent, x + spanSize, y + fm.descent);
        canvas.drawRoundRect(rectF, radiusPx, radiusPx, paint);
        paint.setColor(textColor);
        // FIXME: 2018/12/28 绘制文本，在 OPPO A57 上有可能变成 OBJ
        canvas.drawText(text, start, end, x + paddingPx, y, paint);
    }

}

