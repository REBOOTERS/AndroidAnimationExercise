package home.smart.fly.animations;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bilibili.magicasakura.utils.ThemeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;

import home.smart.fly.animations.interfaces.ActivityLifecycleSimpleCallbacks;

/**
 * Created by rookie on 2017-03-08.
 */

public class MyApplication extends MultiDexApplication implements ThemeUtils.switchColor {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            installLeakCanary();
        }

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
        Fresco.initialize(this);

        ThemeUtils.setSwitchColor(this);
    }

    private RefWatcher installLeakCanary() {
        // ignore some thing
        ExcludedRefs excludedRefs = AndroidExcludedRefs
                .createAndroidDefaults()
                .instanceField("home.smart.fly.animations.AppStartActivity","mContext")
                .build();



        RefWatcher refWatcher= LeakCanary.refWatcher(this)
                .watchDelay(10, TimeUnit.SECONDS)
                .watchActivities(false)
                .excludedRefs(excludedRefs)
                .buildAndInstall();


        // ignore specifice activity classes
        registerActivityLifecycleCallbacks(new ActivityLifecycleSimpleCallbacks(){
            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                if (activity instanceof AppStartActivity) {
                    return;
                }

                refWatcher.watch(activity);
            }
        });

        return  refWatcher;
    }


    @Override
    public int replaceColorById(Context context, int colorId) {
        return 0;
    }

    @Override
    public int replaceColor(Context context, int color) {
        return 0;
    }
}
