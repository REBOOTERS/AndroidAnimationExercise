package com.engineer.imitate.util

import android.os.SystemClock
import java.util.concurrent.TimeUnit

class Throttle(
    skipDuration: Long,
    timeUnit: TimeUnit
) {
    private val delayMilliseconds: Long
    private var oldTime = 0L

    init {
        if (skipDuration < 0) {
            delayMilliseconds = 0
        } else {
            delayMilliseconds = timeUnit.toMillis(skipDuration)
        }
    }

    fun needSkip(): Boolean {
        val nowTime = SystemClock.elapsedRealtime()
        val intervalTime = nowTime - oldTime
        if (oldTime == 0L || intervalTime >= delayMilliseconds) {
            oldTime = nowTime
            return false
        }

        return true
    }
}