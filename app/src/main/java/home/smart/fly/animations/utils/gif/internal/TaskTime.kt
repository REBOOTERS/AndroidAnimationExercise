package home.smart.fly.animations.utils.gif.internal

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 07-06-2019
 */
class TaskTime {

    private var start = 0L

    private val MIL_SECOND = 1000

     init {
         start = System.currentTimeMillis()
     }

    fun release(name: String) {
        val spend = System.currentTimeMillis() - start
        val second = spend / 1000F
        System.out.printf("方法 :%s 耗时 %f ms\n", name, second)
    }

}