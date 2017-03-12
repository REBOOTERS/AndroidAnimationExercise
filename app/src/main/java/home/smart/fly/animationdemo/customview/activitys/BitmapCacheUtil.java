package home.smart.fly.animationdemo.customview.activitys;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**
 * Created by rookie on 2017-03-08.
 */

public class BitmapCacheUtil {


    private static final float M_RATE = 1024 * 1024;
    private static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / M_RATE);
    private static int cacheSize = maxMemory / 8;
    private static LruCache<String, Bitmap> mBitmapLruCache = new LruCache<>(cacheSize);

    private BitmapCacheUtil() {
    }


    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mBitmapLruCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return mBitmapLruCache.get(key);
    }


}
