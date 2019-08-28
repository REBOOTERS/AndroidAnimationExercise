package com.engineer.dateview.interfaces;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Lifecycle;

import com.engineer.dateview.api.UpdateModelDelegate;

/**
 * Created on 2019/6/27.
 *
 * @author rookie
 */
public class MyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "MyActivityLifecycleCall";


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.e(TAG, "onActivityCreated: " + activity.getClass().getCanonicalName());
        UpdateModelDelegate.saveLifeCycleData(activity, Lifecycle.Event.ON_CREATE);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.e(TAG, "onActivityStarted: " + activity.getClass().getCanonicalName());
        UpdateModelDelegate.saveLifeCycleData(activity, Lifecycle.Event.ON_START);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.e(TAG, "onActivityResumed: " + activity.getClass().getCanonicalName());
        UpdateModelDelegate.saveLifeCycleData(activity, Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(TAG, "onActivityPaused: " + activity.getClass().getCanonicalName());
        UpdateModelDelegate.saveLifeCycleData(activity, Lifecycle.Event.ON_PAUSE);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e(TAG, "onActivityStopped: " + activity.getClass().getCanonicalName());
        UpdateModelDelegate.saveLifeCycleData(activity, Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e(TAG, "onActivitySaveInstanceState: " + activity.getClass().getCanonicalName());
        UpdateModelDelegate.saveLifeCycleData(activity, Lifecycle.Event.ON_ANY);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e(TAG, "onActivityDestroyed: " + activity.getClass().getCanonicalName());
        UpdateModelDelegate.saveLifeCycleData(activity, Lifecycle.Event.ON_DESTROY);
    }



}
