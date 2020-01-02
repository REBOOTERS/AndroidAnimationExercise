package home.smart.fly.animations.internal.core

import android.util.Log
import java.util.*

object BeautyLog {

    private const val TAG = "Cat"
    private const val TOP_LEFT_CORNER = '┌'
    private const val BOTTOM_LEFT_CORNER = '└'
    private const val HORIZONTAL_LINE = '│'
    private const val DOUBLE_DIVIDER = "───────────────────────────────────------"
    private const val TOP_BORDER =
        TOP_LEFT_CORNER.toString() + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    private const val BOTTOM_BORDER =
        BOTTOM_LEFT_CORNER.toString() + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    private const val CLASS_NAME_FORMAT = "%s class's name:       %s"
    private const val METHOD_NAME_FORMAT = "%s method's name:      %s"
    private const val ARGUMENT_FORMAT = "%s method's arguments: %s"
    private const val RESULT_FORMAT = "%s method's result:    %s"
    private const val COST_TIME_FORMAT = "%s method cost time:   %.2f ms"

    fun printMethodInfo(methodInfo: MethodInfo) {
        println()
        Log.e("0$TAG", TOP_BORDER)
        Log.e("1$TAG", String.format(CLASS_NAME_FORMAT, HORIZONTAL_LINE, methodInfo.className))
        Log.e("2$TAG", String.format(METHOD_NAME_FORMAT, HORIZONTAL_LINE, methodInfo.methodName))
        Log.e("3$TAG", String.format(ARGUMENT_FORMAT, HORIZONTAL_LINE, methodInfo.params))
        Log.e("4$TAG", String.format(RESULT_FORMAT, HORIZONTAL_LINE, methodInfo.result))
        Log.e("5$TAG", String.format(Locale.CHINA, COST_TIME_FORMAT, HORIZONTAL_LINE, methodInfo.time))
        Log.e("6$TAG", BOTTOM_BORDER)
        println()
    }
}