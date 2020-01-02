package home.smart.fly.animations.internal.core

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
        method.time = (System.nanoTime() - startTime)/1000000f
        BeautyLog.printMethodInfo(method)
    }

}