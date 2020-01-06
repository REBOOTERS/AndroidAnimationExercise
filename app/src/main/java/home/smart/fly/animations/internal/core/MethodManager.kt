package home.smart.fly.animations.internal.core

import android.util.Log
import home.smart.fly.animations.BuildConfig
import java.util.*

/**
 * @author rookie
 * @since 01-02-2020
 */
object MethodManager {

    private val methodWareHouse = Vector<MethodInfo>(1024)

    @JvmStatic
    fun start(): Int {
        methodWareHouse.add(MethodInfo())
        return methodWareHouse.size - 1
    }

    @JvmStatic
    fun addParams(param: Any?, index: Int) {
        val method = methodWareHouse[index]
        method.params.add(param)
    }

    @JvmStatic
    fun end(result: Any?, className: String, methodName: String, startTime: Long, index: Int) {
        val method = methodWareHouse[index]
        method.className = className
        method.methodName = methodName
        method.result = result
        method.time = (System.nanoTime() - startTime) / 1000000f
//        debugMessage(result, className, methodName, startTime, index)
        BeautyLog.printMethodInfo(method)
    }

    private fun debugMessage(
        result: Any?,
        className: String,
        methodName: String,
        startTime: Long,
        index: Int
    ) {
        if (BuildConfig.DEBUG) {
            Log.e(
                "cat",
                "methodName: $methodName,startTime: $startTime, index: $index,time: ${System.nanoTime()}}"
            )
            Log.e("cat", methodWareHouse.toString())
        }
    }

}