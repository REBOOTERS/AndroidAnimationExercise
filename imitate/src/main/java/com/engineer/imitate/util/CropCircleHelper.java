package com.engineer.imitate.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created on 2021/5/15.
 *
 * @author rookie
 */
public class CropCircleHelper {
    public static Bitmap cropCircle(Drawable drawable, int size) {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, size, size);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);
        Bitmap bitmap1 = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap1);
        canvas1.drawCircle(size / 2f, size / 2f, size / 2f, paint);
        drawable.draw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bitmap1, 0f, 0f, paint);
        bitmap1.recycle();
        return bitmap;
    }

    public static Bitmap cropCircle(Bitmap drawable, int size) {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);
        Rect target = new Rect(0, 0, size, size);
        Bitmap bitmap1 = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(bitmap1);
        paint.setColor(Color.RED);
        canvas1.drawCircle(size / 2f, size / 2f, size / 2f, paint);
        canvas.drawBitmap(drawable, null, target, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bitmap1, 0f, 0f, paint);
        bitmap1.recycle();
        return bitmap;
    }
}
