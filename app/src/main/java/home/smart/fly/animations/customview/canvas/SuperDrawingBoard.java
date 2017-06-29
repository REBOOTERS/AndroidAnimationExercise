package home.smart.fly.animations.customview.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Rookie on 2017/6/29.
 */

public class SuperDrawingBoard extends ViewGroup {


    public SuperDrawingBoard(Context context) {
        super(context);
    }

    public SuperDrawingBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SuperDrawingBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GREEN);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
