package com.engineer.imitate

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.alibaba.android.arouter.launcher.ARouter
import com.didichuxing.doraemonkit.DoraemonKit
import com.engineer.imitate.interfaces.SimpleActivityCallback
import com.engineer.imitate.ui.fragments.di.DaggerApplicationComponent
import com.engineer.imitate.util.SpUtil
import com.engineer.imitate.util.SystemUtil
import com.engineer.imitate.util.TextUtil
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.stetho.Stetho

/**
 *
 * @author: Rookie
 * @date: 2018-08-21 19:14
 * @version V1.0
 */
@SuppressLint("LogNotTimber")
class ImitateApplication : Application() {

    val applicationComponent = DaggerApplicationComponent.create()

    companion object {
        lateinit var application: Context
        val TAG = "activity_life"
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.d(TAG, "attachBaseContext() called with: base = $base")

    }

    private var view: View? = null
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate() called")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.d(TAG, "process name " + getProcessName())
        }
        Stetho.initializeWithDefaults(this)
        WebView.setWebContentsDebuggingEnabled(true)
        if (SpUtil(this).getBool(SpUtil.KEY_THEME_NIGHT_ON)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        DoraemonKit.disableUpload()
        DoraemonKit.install(this)

        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }

        ARouter.init(this)
        try {
            Fresco.initialize(this)
        } catch (ignore: Exception) {
        }
        kotlinTest()

        application = this

        registerActivityLifecycleCallbacks(object : SimpleActivityCallback() {})

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {
            val TAG = "ProcessLifecycleOwner"

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                Log.d(TAG, "onCreate() called")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                Log.d(TAG, "onResume() called")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                Log.d(TAG, "onPause() called")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onStop() {
                Log.d(TAG, "onStop() called")
            }
        })

        val model = SystemUtil.getSystemModel()
        Log.e(TAG, "onCreate() called model $model")
        val brand = SystemUtil.getDeviceBrand()
        Log.e(TAG, "onCreate() called brand $brand")
    }


    private fun kotlinTest() {
        var result = null == true
        println("null is true ? $result")
        result = null == false
        println("null is false? $result")
        result = null != false
        println("null not false? $result")
        result = null != true
        println("null not true? $result")
        if (view != null) {
            println("result ==" + view.hashCode())
        } else {
            println("result is null")
        }

        testHighFun()

        val json = TextUtil.getJson()
//        Log.e("json", "json form json-dsl is : $json")
    }


    // <editor-fold defaultstate="collapsed" desc="高阶函数测试">

    /**
     * 高阶函数，接受参数，返回对参数的处理结果
     */
    private fun sum(dodo: (x: Int, y: Int) -> Int): Int {
        val result = dodo(1, 2)
        println(result)
        return result
    }

    /**
     * 高阶函数，不接受参数，只是做一件事情，类似装饰者模式的感觉
     */
    private fun sum1(x: Int, y: Int, dodo: () -> Unit): Int {
        dodo()
        return x + y
    }

    private fun sum2(x: Int, y: Int, dodo: (Int, Int) -> Int) {
        val result = dodo(x, y)
        println("result from sum2 $result")
    }

    private fun testHighFun() {
        val s = sum { x, y -> x + y }
        println("result from sum $s")


        val a = 1
        val b = 2
        val sum1 = sum1(a, b, dodo = {
            if (a < b) {
                println("this is decoration a < b")
            } else {
                println("this is decoration a >= b")
            }
        })
        println("result from sum1 $sum1")

        sum2(a, b, dodo = { x, y ->
            (x * y)
        })
    }
    // </editor-fold>

}