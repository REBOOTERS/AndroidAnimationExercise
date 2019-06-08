package home.smart.fly.animations;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.utils.PackageUtils;
import com.engineer.dateview.ActivityRepository;
import com.engineer.dateview.model.ActModel;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.mmkv.MMKV;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import home.smart.fly.animations.interfaces.ActivityLifecycleSimpleCallbacks;
import home.smart.fly.animations.ui.activity.InputActivity;
import home.smart.fly.animations.utils.AppUtils;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: Rookie
 * @date: 2018-10-12
 * @desc
 */
public class DebugMyApplication extends MyApplication {

    String TAG = "ActLifecycleCallbacks";
    ActivityRepository repository;
    private MyActivityLifecycleCallbacks mMyActivityLifecycleCallbacks;

    @Override
    protected RefWatcher installLeakCanary() {
        ExcludedRefs excludedRefs = AndroidExcludedRefs
                .createAndroidDefaults()
                .instanceField("home.smart.fly.animations.AppStartActivity", "mContext")
                .build();


        RefWatcher refWatcher = LeakCanary.refWatcher(this)
                .watchDelay(10, TimeUnit.SECONDS)
                .watchActivities(false)
                .excludedRefs(excludedRefs)
                .buildAndInstall();


        // ignore specifice activity classes
        registerActivityLifecycleCallbacks(new ActivityLifecycleSimpleCallbacks() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                if (activity instanceof AppStartActivity) {
                    return;
                }

                refWatcher.watch(activity);
            }
        });

        return refWatcher;
    }


    private boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 监听 Activity 的生命周期
     */
    @Override
    public void logLifeCycleCallBacks() {


        repository = new ActivityRepository(this);

        mMyActivityLifecycleCallbacks = new MyActivityLifecycleCallbacks();
        unregisterActivityLifecycleCallbacks(mMyActivityLifecycleCallbacks);
        registerActivityLifecycleCallbacks(mMyActivityLifecycleCallbacks);
    }


    private void saveLifeCycleData(Activity activity, ActivityRepository repository, Lifecycle.Event event) {

        String name = activity.getClass().getCanonicalName();
        Log.e("room", "name ==" + name);
        Log.e("room", "event ==" + event.name());

        Disposable d = Observable.create((ObservableOnSubscribe<ActModel>) emitter -> {
            ActModel actModel = repository.getActivityByName(name);
            if (actModel == null) {
                actModel = new ActModel();
                actModel.setName(name);
            }

            emitter.onNext(actModel);
            emitter.onComplete();

        })

                .subscribeOn(Schedulers.io())
                .map(actModel -> {
                    int count;
                    switch (event) {
                        case ON_CREATE:
                            count = actModel.getOnActivityCreateCount();
                            actModel.setOnActivityCreateCount(++count);
                            return actModel;
                        case ON_START:
                            count = actModel.getOnActivityStartedCount();
                            actModel.setOnActivityStartedCount(++count);
                            return actModel;
                        case ON_RESUME:
                            count = actModel.getOnActivityResumedCount();
                            actModel.setOnActivityResumedCount(++count);
                            return actModel;
                        case ON_PAUSE:
                            count = actModel.getOnActivityPausedCount();
                            actModel.setOnActivityPausedCount(++count);
                            return actModel;
                        case ON_STOP:
                            count = actModel.getOnActivityStoppedCount();
                            actModel.setOnActivityPausedCount(++count);
                            return actModel;
                        case ON_DESTROY:
                            count = actModel.getOnActivityDestroyedCount();
                            actModel.setOnActivityDestroyedCount(++count);
                            return actModel;
                        default:
                            count = actModel.getOnActivitySaveInstanceStateCount();
                            actModel.setOnActivitySaveInstanceStateCount(++count);
                            return actModel;

                    }
                })
                .subscribe(actModel -> {
                            Log.e("room", "start insert : " + actModel.toString());
                            repository.insert(actModel);
                        },
                        throwable -> {
                            Log.e("room", "wrong with " + throwable.getMessage());
                            throwable.printStackTrace();
                        });

    }

    private class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.e(TAG, "onActivityCreated: " + activity.getClass().getCanonicalName());
            Log.e(TAG, "is main " + isMainThread());
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
    }
}
