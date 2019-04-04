package com.engineer.imitate.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.view.PixelCopy;
import android.view.View;

import org.jetbrains.annotations.NotNull;

/**
 * @author: zhuyongging
 * @since: 2019-03-21
 */
public class ViewUtils {

    public interface Callback {
        void onResult(@NotNull Bitmap bitmap);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getBitmapFormView(View view, Activity activity, Callback callback) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);

        int[] locations = new int[2];
        view.getLocationInWindow(locations);
        Rect rect = new Rect(locations[0], locations[1], locations[0] + view.getWidth(), locations[1] + view.getHeight());

        PixelCopy.request(activity.getWindow(), rect, bitmap, copyResult -> {
            if (copyResult == PixelCopy.SUCCESS) {
                callback.onResult(bitmap);
            }
        }, new Handler(Looper.getMainLooper()));
    }


    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap ;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        bitmap = view.getDrawingCache();
//        view.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
