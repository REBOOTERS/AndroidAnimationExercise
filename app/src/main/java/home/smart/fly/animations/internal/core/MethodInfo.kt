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
)  {
    override fun equals(other: Any?): Boolean {
        val m:MethodInfo = other as MethodInfo
        return m.methodName == this.methodName
    }

    override fun toString(): String {
        return "MethodInfo(className='$className', methodName='$methodName', result=$result, time=$time, params=$params)"
    }


}