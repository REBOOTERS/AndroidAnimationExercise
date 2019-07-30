package com.engineer.imitate

import android.app.Application
import android.os.Build
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import java.lang.Exception

/**
 *
 * @author: Rookie
 * @date: 2018-08-21 19:14
 * @version V1.0
 */
class ImitateApplication : Application() {
    private var view: View? = null
    override fun onCreate() {
        super.onCreate()
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
        try {
            Fresco.initialize(this)
        } catch (ignore: Exception) { }
        kotlinTest()
    }



    private fun kotlinTest() {

        if (view != null) {
            println("result ==" + view.hashCode())
        } else {
            println("result is null")
        }
    }
}