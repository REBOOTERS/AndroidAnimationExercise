package home.smart.fly.animations;

import android.app.Application;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.tencent.mmkv.MMKV;

import home.smart.fly.animations.internal.annotations.Path;
import hugo.weaving.DebugLog;

/**
 * Created by rookie on 2017-03-08.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    private static MyApplication sInstance;

    @Override
    @DebugLog
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Log.e("TAG-----ZYQ", "onCreate1: " + this.getClass().getName());
        Looper.myQueue().addIdleHandler(() -> {
            Log.e("TAG-----ZYQ", "queueIdle:");
            Fresco.initialize(MyApplication.this);

            WebView.setWebContentsDebuggingEnabled(true);
            Stetho.initializeWithDefaults(MyApplication.this);
            return false;
        });
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(MyApplication.getsInstance());
        String dir = MMKV.initialize(MyApplication.this);
        Log.e("application", "onCreate: mmkv.dir==" + dir);

        logLifeCycleCallBacks();
        Log.e("TAG-----ZYQ", "onCreate2: " + this.getClass().getName());
    }

    @Path(value = "", level = 1100)
    protected void logLifeCycleCallBacks() {
    }

    public static MyApplication getsInstance() {
        return sInstance;
    }
}
