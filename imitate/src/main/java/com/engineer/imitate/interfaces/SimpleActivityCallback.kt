package com.engineer.imitate.interfaces

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

/**
 * @author Rookie
 * @since 07-01-2020
 */
@SuppressLint("LogNotTimber")
open class SimpleActivityCallback : Application.ActivityLifecycleCallbacks {
    val TAG = "activity-life"

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG, "onActivityPaused() called with: activity = $activity")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG, "onActivityStarted() called with: activity = $activity")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG, "onActivityDestroyed() called with: activity = $activity")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.d(
            TAG,
            "onActivitySaveInstanceState() called with: activity = $activity, outState = $outState"
        )
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG, "onActivityStopped() called with: activity = $activity")
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.d(
            TAG,
            "onActivityCreated() called with: activity = $activity, savedInstanceState = $savedInstanceState"
        )
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG, "onActivityResumed() called with: activity = $activity")
    }
}