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


    /**
     * 监听 Activity 的生命周期
     */
    private void logLifeCycleCallBacks() {

        ActivityRepository repository = new ActivityRepository(this);

        String TAG = "ActLifecycleCallbacks";
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e(TAG, "onActivityCreated: " + activity.getClass().getCanonicalName());
                saveLifeCycleData(activity, repository, Lifecycle.Event.ON_CREATE);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.e(TAG, "onActivityStarted: " + activity.getClass().getCanonicalName());
                saveLifeCycleData(activity, repository, Lifecycle.Event.ON_START);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Log.e(TAG, "onActivityResumed: " + activity.getClass().getCanonicalName());
                saveLifeCycleData(activity, repository, Lifecycle.Event.ON_RESUME);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                Log.e(TAG, "onActivityPaused: " + activity.getClass().getCanonicalName());
                saveLifeCycleData(activity, repository, Lifecycle.Event.ON_PAUSE);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.e(TAG, "onActivityStopped: " + activity.getClass().getCanonicalName());
                saveLifeCycleData(activity, repository, Lifecycle.Event.ON_STOP);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Log.e(TAG, "onActivitySaveInstanceState: " + activity.getClass().getCanonicalName());
                saveLifeCycleData(activity, repository, Lifecycle.Event.ON_ANY);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e(TAG, "onActivityDestroyed: " + activity.getClass().getCanonicalName());
                saveLifeCycleData(activity, repository, Lifecycle.Event.ON_DESTROY);
            }
        });
    }

    private void saveLifeCycleData(Activity activity, ActivityRepository repository, Lifecycle.Event event) {
        Disposable d = repository.getActivityByName(activity.getClass().getCanonicalName())
                .subscribeOn(Schedulers.io())
                .defaultIfEmpty(new ActModel())
                .map(actModel -> {
                    int count;
                    switch (event) {
                        case ON_CREATE:
                            count = actModel.getOnActivityCreateCount();
                            actModel.setOnActivityCreateCount(++count);
                            break;
                        case ON_START:
                            count = actModel.getOnActivityStartedCount();
                            actModel.setOnActivityStartedCount(++count);
                            break;
                        case ON_RESUME:
                            count = actModel.getOnActivityResumedCount();
                            actModel.setOnActivityResumedCount(++count);
                            break;
                        case ON_PAUSE:
                            count = actModel.getOnActivityPausedCount();
                            actModel.setOnActivityPausedCount(++count);
                            break;
                        case ON_STOP:
                            count = actModel.getOnActivityStoppedCount();
                            actModel.setOnActivityPausedCount(++count);
                            break;
                        case ON_DESTROY:
                            count = actModel.getOnActivityDestroyedCount();
                            actModel.setOnActivityDestroyedCount(++count);
                            break;
                        default:
                            count = actModel.getOnActivitySaveInstanceStateCount();
                            actModel.setOnActivitySaveInstanceStateCount(++count);

                    }
                    return actModel;
                })
                .subscribe(actModel ->
                                repository.insert(actModel),
                        throwable -> {
                            Log.e("room", "wrong with " + throwable.getMessage());
                            throwable.printStackTrace();
                        });
    }


    protected RefWatcher installLeakCanary() {
        // ignore some thing
        return RefWatcher.DISABLED;
    }
}
