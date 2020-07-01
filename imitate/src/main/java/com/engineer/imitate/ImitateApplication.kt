package com.engineer.imitate

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.webkit.WebView
import com.alibaba.android.arouter.launcher.ARouter
import com.engineer.imitate.interfaces.SimpleActivityCallback
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

    companion object {
        lateinit var application: Context
        val TAG = "activity-life"
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
    }


    private fun kotlinTest() {

        if (view != null) {
            println("result ==" + view.hashCode())
        } else {
            println("result is null")
        }

        testHighFun()

        val json = TextUtil.getJson()
        Log.e("json", "json form json-dsl is : $json")
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