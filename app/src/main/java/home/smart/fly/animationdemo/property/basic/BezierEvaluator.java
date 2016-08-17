package home.smart.fly.animationdemo.property.basic;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * Created by co-mall on 2016/8/12.
 */
public class BezierEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
//        Log.e("farction", "fraction value is " + fraction);
        Point point;
        Point P0 = (Point) startValue;
        Point P2 = (Point) endValue;
        Point P1 = new Point(Math.abs(P2.x - P0.x) * 2 / 3, P0.y-100);
        int x = (int) ((1 - fraction) * (1 - fraction) * P0.x + 2 * fraction * (1 - fraction) * P1.x + fraction * fraction * P2.x);
        int y = (int) ((1 - fraction) * (1 - fraction) * P0.y + 2 * fraction * (1 - fraction) * P1.y + fraction * fraction * P2.y);
        point = new Point(x, y);
        return point;
    }
}
