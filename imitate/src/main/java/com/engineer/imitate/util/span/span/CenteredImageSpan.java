package com.engineer.imitate.util.span.span;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;

/**
 * ImageSpan与TextView中的text居中对齐问题解决（无论TextView设置行距与否）
 * 参阅：
 * https://www.cnblogs.com/withwind318/p/5541267.html
 * https://www.cnblogs.com/tianzhijiexian/p/4297664.html
 * <p>
 * Create by liyujiang on 2018/12/28 10:08
 */
public class CenteredImageSpan extends ImageSpan {

    public CenteredImageSpan(Context context, final int drawableRes) {
        super(context, drawableRes);
    }

    /**
     * x，要绘制的image的左边框到textview左边框的距离。
     * y，要替换的文字的基线坐标，即基线到textview上边框的距离。
     * top，替换行的最顶部位置。
     * bottom，替换行的最底部位置。注意，textview中两行之间的行间距是属于上一行的，所以这里bottom是指行间隔的底部位置。
     * paint，画笔，包含了要绘制字体的度量信息。
     * 这几个参数含义在代码中找不到说明，写了个demo测出来的。top和bottom参数只是解释下，函数里面用不上。
     * 然后解释下代码逻辑：
     * getDrawable获取要绘制的image，getBounds是获取包裹image的矩形框尺寸；
     * y + fm.descent得到字体的descent线坐标；
     * y + fm.ascent得到字体的ascent线坐标；
     * 两者相加除以2就是两条线中线的坐标；
     * b.getBounds().bottom是image的高度（试想把image放到原点），除以2即高度一半；
     * 前面得到的中线坐标减image高度的一半就是image顶部要绘制的目标位置；
     * 最后把目标坐标传递给canvas.translate函数就可以了，至于这个函数的理解先不管了
     */
    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
                     int top, int y, int bottom, @NonNull Paint paint) {
        Drawable drawable = getDrawable();
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2 - drawable.getBounds().bottom / 2;
        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }

}
