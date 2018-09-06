package home.smart.fly.animations;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bilibili.magicasakura.utils.ThemeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by rookie on 2017-03-08.
 */

public class MyApplication extends MultiDexApplication implements ThemeUtils.switchColor {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }

        ARouter.init(this);
        Fresco.initialize(this);

        ThemeUtils.setSwitchColor(this);
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
