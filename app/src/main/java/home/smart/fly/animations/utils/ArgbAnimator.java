package home.smart.fly.animations.utils;

/**
 * Created by you on 2017/5/5.
 * 颜色过渡器
 */

public class ArgbAnimator {

    /**
     * 起始颜色值, 结束颜色值
     */
    private int startColor, endColor;
    /**
     * 　起始ARGB值
     */
    private int startA, startR, startG, startB;
    /**
     * 　结束ARGB值
     */
    private int endA, endR, endG, endB;

    /**
     * 构造函数
     *
     * @param startColor 　起始颜色值
     * @param endColor   　结束颜色值
     */
    public ArgbAnimator(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        startA = (startColor >> 24) & 0xff;
        startR = (startColor >> 16) & 0xff;
        startG = (startColor >> 8) & 0xff;
        startB = startColor & 0xff;

        endA = (endColor >> 24) & 0xff;
        endR = (endColor >> 16) & 0xff;
        endG = (endColor >> 8) & 0xff;
        endB = endColor & 0xff;
    }

    /**
     * 获取动画段中的颜色
     *
     * @param fraction 0-1.0f
     * @return
     */
    public int getFractionColor(float fraction) {
        if (fraction <= 0)
            return startColor;
        if (fraction >= 1.0f)
            return endColor;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

}
