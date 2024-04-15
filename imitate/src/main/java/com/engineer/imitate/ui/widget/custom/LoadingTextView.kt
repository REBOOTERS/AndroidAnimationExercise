package com.engineer.imitate.ui.widget.custom

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * 用于显示 。。。 的 loading 态 TextView
 */
class LoadingTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, style: Int = 0
) : AppCompatTextView(context, attrs, style) {


    private var dotCount = 0
    private val handler = Handler(Looper.getMainLooper())

    init {
        text = ""
    }

    private val loadingTask = object : Runnable {
        override fun run() {
            if (dotCount < 4) {
                this@LoadingTextView.append(".")
                dotCount++
            } else {
                dotCount = 1
                this@LoadingTextView.text = "."// 重置为单个点
            }
            handler.postDelayed(this, 500); // 每 500 毫秒更新一次
        }

    }

    fun show() {
        hide()
        handler.post(loadingTask)
    }

    fun hide() {
        text = ""
        handler.removeCallbacks(loadingTask)
        text = ""
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        hide()
    }

}