package com.engineer.imitate.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.engineer.imitate.ui.activity.DateAndTimePickerActivity

/**
 * @author rookie
 * @since 12-20-2019
 */
class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("LogNotTimber")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("picker", ":onReceive")
        intent?.apply {
            if (action == DateAndTimePickerActivity.alarm_log) {
                Log.e("picker", ": $intent, threadName ${Thread.currentThread().name}")
            }
        }
    }
}