package home.smart.fly.animations.tradition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rookie on 2016/10/11.
 */

public class AxisView extends View {
    private Paint textPaint;
    private Paint linePaint;

    public AxisView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public AxisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public AxisView(Context context) {
        super(context);
        initPaint();
    }

    private void initPaint() {
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25);
        //
        linePaint = new Paint();
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        canvas.drawText("(0,0)", 20, 20, textPaint);
        canvas.drawLine(25, 25, 650, 25, linePaint);
        canvas.drawLine(25, 25, 25, 550, linePaint);
        canvas.drawText("pivotX", 400, 20, textPaint);
        canvas.drawText("pivotY", -5, 560, textPaint);
    }
}
