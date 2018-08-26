package com.engineer.imitate

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary

/**
 *
 * @author: Rookie
 * @date: 2018-08-21 19:14
 * @version V1.0
 */
class ImitateApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        ARouter.init(this)

        Stetho.initializeWithDefaults(this)

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }

        ARouter.init(this)
        Fresco.initialize(this)
    }
}