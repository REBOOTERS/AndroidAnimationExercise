package home.smart.fly.animations.internal.core

/**
 * @author rookie
 * @since 01-02-2020
 */
object MethodManager {

    private val methodWareHouse = ArrayList<MethodInfo>(1024)

    @JvmStatic
    fun start(): Int {
        methodWareHouse.add(MethodInfo())
        return methodWareHouse.size - 1
    }

    @JvmStatic
    fun addParams(param: Any?, id: Int) {
        val method = methodWareHouse[id]
        method.params.add(param)
    }

    @JvmStatic
    fun end(result: Any?, className: String, methodName: String, startTime: Long, id: Int) {
        val method = methodWareHouse[id]
        method.className = className
        method.methodName = methodName
        method.result = result
        method.time = (System.nanoTime() - startTime)/1000000f
        BeautyLog.printMethodInfo(method)
    }

}