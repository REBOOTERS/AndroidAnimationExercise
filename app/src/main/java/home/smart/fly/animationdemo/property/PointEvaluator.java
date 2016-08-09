package home.smart.fly.animationdemo.property;

import android.animation.TypeEvaluator;
import android.util.Log;

/**
 * Created by co-mall on 2016/8/9.
 */
public class PointEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());
        Log.e("fraction", "----------->" + fraction);
        Point point = new Point(x, y);
        return point;
    }
}
