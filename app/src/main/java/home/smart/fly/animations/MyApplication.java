package home.smart.fly.animations;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.lifecycle.Lifecycle;
import androidx.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.didichuxing.doraemonkit.DoraemonKit;
import com.engineer.dateview.ActivityRepository;
import com.engineer.dateview.model.ActModel;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.mmkv.MMKV;

import hugo.weaving.DebugLog;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rookie on 2017-03-08.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    @DebugLog
    public void onCreate() {
        super.onCreate();
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

        WebView.setWebContentsDebuggingEnabled(true);


        DoraemonKit.install(this);

        String dir = MMKV.initialize(this);
        Log.e("application", "onCreate: mmkv.dir==" + dir);


        logLifeCycleCallBacks();
    }

    protected  void logLifeCycleCallBacks() {}


    protected RefWatcher installLeakCanary() {
        // ignore some thing
        return RefWatcher.DISABLED;
    }
}
