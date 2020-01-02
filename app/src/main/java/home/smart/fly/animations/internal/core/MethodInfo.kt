package home.smart.fly.animations.internal.core

/**
 * @author rookie
 * @since 01-02-2020
 */
data class MethodInfo(
    var className: String = "",
    var methodName: String = "",
    var result: Any? = "",
    var time: Float = 0f,
    var params: ArrayList<Any?> = ArrayList()
)