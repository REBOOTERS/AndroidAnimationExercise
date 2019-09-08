package home.smart.fly.animations;

import android.app.Application;
import android.util.Log;
import android.webkit.WebView;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.mmkv.MMKV;

import hugo.weaving.DebugLog;

/**
 * Created by rookie on 2017-03-08.
 */

public class MyApplication extends Application {
    @Override
    @DebugLog
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Stetho.initializeWithDefaults(this);

        if (!LeakCanary.isInAnalyzerProcess(this)) {
//            installLeakCanary();
        }

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
        Fresco.initialize(this);
        String dir = MMKV.initialize(this);
        Log.e("application", "onCreate: mmkv.dir==" + dir);
        WebView.setWebContentsDebuggingEnabled(true);


//        DoraemonKit.install(this);




        logLifeCycleCallBacks();
    }

    protected void logLifeCycleCallBacks() {
    }

    protected RefWatcher installLeakCanary() {
        // ignore some thing
        return RefWatcher.DISABLED;
    }
}
