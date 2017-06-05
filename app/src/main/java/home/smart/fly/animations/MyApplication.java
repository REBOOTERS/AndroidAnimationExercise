package home.smart.fly.animations;

import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

/**
 * Created by rookie on 2017-03-08.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    Handler mHandler;
}
