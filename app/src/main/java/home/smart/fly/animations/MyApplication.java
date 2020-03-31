package home.smart.fly.animations;

import android.app.Application;
import android.os.Debug;
import android.util.Log;
import android.webkit.WebView;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.tencent.mmkv.MMKV;

import home.smart.fly.animations.internal.annotations.Path;
import hugo.weaving.DebugLog;

/**
 * Created by rookie on 2017-03-08.
 */

public class MyApplication extends Application {
    @Override
    @DebugLog
    public void onCreate() {
        super.onCreate();

        Debug.startMethodTracing("sample");

        MultiDex.install(this);
        Stetho.initializeWithDefaults(this);

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
        Fresco.initialize(this);
        String dir = MMKV.initialize(this);
        Log.e("application", "onCreate: mmkv.dir==" + dir);
        WebView.setWebContentsDebuggingEnabled(true);


        DoraemonKit.disableUpload();
        DoraemonKit.install(this);
//        DoraemonKit.hide();
        logLifeCycleCallBacks();

        Debug.stopMethodTracing();
    }

    @Path(value = "", level = 1100)
    protected void logLifeCycleCallBacks() {
    }
}
