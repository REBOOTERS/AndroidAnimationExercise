package home.smart.fly.animationdemo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by rookie on 2017-03-08.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
