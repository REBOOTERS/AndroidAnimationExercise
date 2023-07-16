package com.engineer.imitate

import android.util.Log
import androidx.metrics.performance.FrameData
import androidx.metrics.performance.JankStats

/**
 * https://mp.weixin.qq.com/s/QGR4ZcrhdYi8hiRenRRZfw
 */
class AppOnJankReportListener : JankStats.OnFrameListener {
    private val TAG = "AppOnJankReportListener"

    override fun onFrame(volatileFrameData: FrameData) {
        Log.d(TAG, "onFrame() called with: volatileFrameData = $volatileFrameData")
        if (volatileFrameData.isJank) {

        }
    }
}