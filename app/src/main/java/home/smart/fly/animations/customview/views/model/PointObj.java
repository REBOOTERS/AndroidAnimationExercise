package home.smart.fly.animations.customview.views.model;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by rookie on 2017/2/16.
 */

public class PointObj extends GraphicObject {
    private PointF center;
    private int pointRadius;

    public PointObj() {
        center = new PointF();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(center.x, center.y, pointRadius, paint);
    }

    public void setCenter(float x, float y) {
        this.center.set(x, y);
    }


    public void setPointRadius(int pointRadius) {
        this.pointRadius = pointRadius;
    }
}
