package com.engineer.android.game.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.engineer.android.game.R;

public class SensorView extends SurfaceView implements SurfaceHolder.Callback {
    public SensorViewDrawThread mvdt;
    Paint paint;
    public float dx;
    public float dy;
    public float dz;
    float x;
    float y;
    float rx;
    float ry;
    float juli2;
    float juli;
    Bitmap yuan;
    Bitmap shang;
    Bitmap zuo;
    Bitmap qiuzuo;
    Bitmap qiushang;
    Bitmap qiuzhong;

    public SensorView(Context context) {
        super(context);
        this.getHolder().addCallback(this);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
        paint.setAntiAlias(true);
        mvdt = new SensorViewDrawThread(this);

        yuan = BitmapFactory.decodeResource(getResources(), R.drawable.yuan);
        zuo = BitmapFactory.decodeResource(getResources(), R.drawable.zuo);
        shang = BitmapFactory.decodeResource(getResources(), R.drawable.shang);
        qiuzuo = BitmapFactory.decodeResource(getResources(), R.drawable.qiuzuo);
        qiushang = BitmapFactory.decodeResource(getResources(), R.drawable.qiushang);
        qiuzhong = BitmapFactory.decodeResource(getResources(), R.drawable.qiuzhong);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(shang, 105, 0, paint);
        canvas.drawBitmap(yuan, 400, 150, paint);
        canvas.drawBitmap(zuo, 0, 0, paint);
        x = dx * 34;
        if (x > 200) x = 200;
        if (x < -200) x = -200;
        canvas.drawBitmap(qiuzuo, 10, 300 + x, paint);
        y = dy * 34;
        if (y > 550) y = 550;
        if (y < -550) y = -550;
        canvas.drawBitmap(qiushang, 610 + y, 3, paint);
        juli = (float) Math.sqrt((dx * 34) * (dx * 34) + (dy * 34) * (dy * 34));
        juli2 = juli / 170;
        if (juli2 <= 1) {
            rx = (dy * 34) / 170;
            ry = (dx * 34) / 170;

        } else {
            if (dy > 0) {
                rx = (float) Math.sqrt(2 * dy * dy / (dx * dx + dy * dy));
            } else {
                rx = -(float) Math.sqrt(2 * dy * dy / (dx * dx + dy * dy));
            }

            ry = dx / dy * rx;
        }

        canvas.drawBitmap(qiuzhong, 630 + rx * 110, 380 + ry * 110, paint);

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mvdt.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {

    }
}