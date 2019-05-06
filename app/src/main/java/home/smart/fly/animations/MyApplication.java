package home.smart.fly.animations;

import android.graphics.Color;
import androidx.multidex.MultiDexApplication;
import android.util.Log;
import android.webkit.WebView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.mmkv.MMKV;

import hugo.weaving.DebugLog;
import jp.wasabeef.takt.Seat;
import jp.wasabeef.takt.Takt;

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

        Takt.stock(this)
                .seat(Seat.TOP_RIGHT)
                .interval(250)
                .color(Color.RED)
                .size(14f)
                .alpha(.5f)
                .listener(fps -> Log.d("Excellent!", fps + " fps"));

        String dir = MMKV.initialize(this);
        Log.e("application", "onCreate: mmkv.dir=="+dir );
    }


    protected RefWatcher installLeakCanary() {
        // ignore some thing
        return RefWatcher.DISABLED;
    }
}
