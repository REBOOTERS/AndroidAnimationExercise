package home.smart.fly.animations;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.Lifecycle;

import com.engineer.dateview.ActivityRepository;
import com.engineer.dateview.model.ActModel;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.AndroidExcludedRefs;
import com.squareup.leakcanary.ExcludedRefs;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import home.smart.fly.animations.interfaces.ActivityLifecycleSimpleCallbacks;
import home.smart.fly.animations.ui.activity.InputActivity;
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

        ActivityRepository repository = new ActivityRepository(this);


        Activity activity = new InputActivity();
        String name = activity.getClass().getCanonicalName();
        Log.e("room", name);

        ActModel actModel = new ActModel();
        actModel.setName(name);
        actModel.setOnActivityCreateCount(1);
        repository.insert(actModel);


        String TAG = "ActLifecycleCallbacks";

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
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
        });
    }

    ActModel tem;
    private void saveLifeCycleData(Activity activity, ActivityRepository repository, Lifecycle.Event event) {
        String name = activity.getClass().getCanonicalName();

        Disposable d = repository.getActivityByName(name)
                .subscribeOn(Schedulers.io())
                .map(model -> {
                    tem = model;
                    if (model == null) {
                        ActModel temp = new ActModel();
                        temp.setName(name);
                        return temp;
                    } else {
                        return model;
                    }
                })
                .map(actModel -> {
                    int count;
                    tem = actModel;
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


}
