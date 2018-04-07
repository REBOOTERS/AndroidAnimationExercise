package home.smart.fly.animations.webview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.WebView

/**
 * author : engineer
 * e-mail : yingkongshi11@gmail.com
 * time   : 2018/1/29
 * desc   :
 * version: 1.0
 */
class CustomWebView : WebView {

    private val TAG: String = "CustomWebView"

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defaultStyle: Int) : super(context,
            attributeSet, defaultStyle)

    var oldY: Float = 0.0f
    var t: Int = 0


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                val Y = event.y
                val deltaY: Float = Y - oldY

                Log.e(TAG, "deltaY==" + deltaY)




                if (deltaY > 0 && this.t == 0) {
                    parent.parent.requestDisallowInterceptTouchEvent(false)
                }
            }

            MotionEvent.ACTION_DOWN -> {
                parent.parent.requestDisallowInterceptTouchEvent(true)
                oldY = event.y
            }
            MotionEvent.ACTION_UP -> {
                parent.parent.requestDisallowInterceptTouchEvent(true)
            }
            else -> {

            }

        }
        return super.onTouchEvent(event)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        Log.e(TAG, "t=" + t)
        this.t = t
        super.onScrollChanged(l, t, oldl, oldt)

    }
}



