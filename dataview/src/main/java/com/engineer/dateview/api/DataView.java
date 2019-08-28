package com.engineer.dateview.api;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import com.engineer.dateview.ui.DataViewActivity;

/**
 * Created on 2019/6/27.
 *
 * @author rookie
 */
public class DataView {

    private static boolean hasInit = false;

    private static ActivityRepository mActivityRepository;

    public static void init(Application application) {
        if (!hasInit) {
            mActivityRepository = new ActivityRepository(application);
            UpdateModelDelegate.injectRepository(mActivityRepository);
            hasInit = true;
        }
    }

    public static ActivityRepository getRepository() {
        if (mActivityRepository == null) {
            throw new RuntimeException("please call init first");
        }
        return mActivityRepository;
    }

    public static void show(Context context) {
        context.startActivity(new Intent(context, DataViewActivity.class));
    }

    private boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
