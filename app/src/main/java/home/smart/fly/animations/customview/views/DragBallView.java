package home.smart.fly.animations.customview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Rookie on 2017/7/18.
 */

public class DragBallView extends View {

    private GestureDetector m_gestureDetector;
    private Paint m_paint;
    //小球的中心点
    private float centerX;
    private float centerY;
    //小球的半径
    private int radius;
    //是否touch在小球上
    private boolean touch_bool;

    public DragBallView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始画笔
        m_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_paint.setStyle(Paint.Style.FILL);
        m_paint.setStrokeWidth(8);
        m_paint.setColor(Color.RED);
        //设置为可点击
        setClickable(true);
        //2,初始化手势类，同时设置手势监听
        m_gestureDetector = new GestureDetector(context, onGestureListener);
        radius = 100;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //3,将touch事件交给gesture处理
        m_gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //判断手指落在了小球上
            if (getDistanceByPoint((int) centerX, (int) centerY, (int) event.getX(), (int) event.getY()) < radius) {
                touch_bool = true;
            } else {
                touch_bool = false;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // 默认圆心在中心点
        if (w > 0) {
            centerX = w / 2;
        }
        if (h > 0) {
            centerY = h / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawCircle(centerX, centerY, radius, m_paint);
    }

    GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (touch_bool) {
                centerY -= distanceY;
                centerX -= distanceX;
                //处理边界问题
                if (centerX < radius) {
                    centerX = radius;
                } else if (centerX > getWidth() - radius) {
                    centerX = getWidth() - radius;
                }
                if (centerY < radius) {
                    centerY = radius;
                } else if (centerY > getHeight() - radius) {
                    centerY = getHeight() - radius;
                }
                //修改圆心后，通知重绘
                postInvalidate();
            }
            return true;
        }
    };

    /**
     * 计算两点间的距离
     */
    private int getDistanceByPoint(int x1, int y1, int x2, int y2) {
        double temp = Math.abs((x2 - x1) * (x2 - x1) - (y2 - y1) * (y2 - y1));
        return (int) Math.sqrt(temp);
    }

}
