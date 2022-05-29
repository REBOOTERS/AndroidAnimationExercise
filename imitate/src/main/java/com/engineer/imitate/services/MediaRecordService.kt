package com.engineer.imitate.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.engineer.imitate.util.ScreenRecordHelper

class MediaRecordService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}