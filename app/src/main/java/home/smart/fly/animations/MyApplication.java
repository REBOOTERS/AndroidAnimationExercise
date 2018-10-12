package home.smart.fly.animations;

import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by rookie on 2017-03-08.
 */

public class MyApplication extends MultiDexApplication  {
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
    }

    protected RefWatcher installLeakCanary() {
        // ignore some thing
        return RefWatcher.DISABLED;
    }
}
