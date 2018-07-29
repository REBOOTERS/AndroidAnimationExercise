package com.engineer.phenix.internal.glide;

import android.app.Activity;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.signature.EmptySignature;
import com.engineer.phenix.internal.glide.cache.OriginalKey;
import com.engineer.phenix.internal.glide.cache.SafeKeyGenerator;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class ImageLoader {

    /**
     * 获取是否有某张原图的缓存
     * 缓存模式必须是：DiskCacheStrategy.SOURCE 才能获取到缓存文件
     */
    public static File getGlideCacheFile(Context context, String url) {
        try {
            OriginalKey originalKey = new OriginalKey(url, EmptySignature.obtain());
            SafeKeyGenerator safeKeyGenerator = new SafeKeyGenerator();
            String safeKey = safeKeyGenerator.getSafeKey(originalKey);
            File file = new File(context.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR);
            DiskLruCache diskLruCache = DiskLruCache.open(file, 1, 1, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
            DiskLruCache.Value value = diskLruCache.get(safeKey);
            if (value != null) {
                return value.getFile(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    public static void cleanDiskCache(final Context context) {

        Observable.empty()
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        Glide.get(context.getApplicationContext()).clearDiskCache();
                    }
                })
                .dispose();
    }
}