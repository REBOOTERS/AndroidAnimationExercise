package home.smart.fly.animations;

import android.app.Activity;

import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;

import home.smart.fly.animations.interfaces.ActivityLifecycleSimpleCallbacks;

/**
 * @author: Rookie
 * @date: 2018-10-12
 * @desc
 */
public class DebugMyApplication extends MyApplication {
    @Override
    protected RefWatcher installLeakCanary() {
        ExcludedRefs excludedRefs = AndroidExcludedRefs
                .createAndroidDefaults()
                .instanceField("home.smart.fly.animations.AppStartActivity", "mContext")
                .build();


        RefWatcher refWatcher = LeakCanary.refWatcher(this)
                .watchDelay(10, TimeUnit.SECONDS)
                .watchActivities(false)
                .excludedRefs(excludedRefs)
                .buildAndInstall();


        // ignore specifice activity classes
        registerActivityLifecycleCallbacks(new ActivityLifecycleSimpleCallbacks() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                if (activity instanceof AppStartActivity) {
                    return;
                }

                refWatcher.watch(activity);
            }
        });

        return refWatcher;
    }

    @Override
    protected void installGodEye() {
        super.installGodEye();
    }

}
