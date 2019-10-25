package com.engineer.imitate.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.appcompat.widget.AppCompatImageView
import com.zdog.library.render.ZdogDrawable

class DynamicImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var isRunning = false

    fun start() {
        if (drawable is ZdogDrawable) {
            (drawable as ZdogDrawable).start()
        }
    }

    fun start(delay: Long = 0) {
        if (drawable is ZdogDrawable) {
            (drawable as ZdogDrawable).start(delay)
        }
    }

    fun resume() {
        if (drawable is ZdogDrawable) {
            (drawable as ZdogDrawable).resume()
        }
    }

    fun pause() {
        if (drawable is ZdogDrawable) {
            (drawable as ZdogDrawable).pause()
        }
    }

    fun cancel() {
        if (drawable is ZdogDrawable) {
            isRunning = (drawable as ZdogDrawable).isRunning()
            (drawable as ZdogDrawable).cancel()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isRunning) {
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancel()
    }

    override fun setVisibility(visibility: Int) {
        if (this.visibility != visibility) {
            if (visibility == View.VISIBLE) {
                if (isRunning) {
                    start()
                }
            } else {
                cancel()
            }
        }

        super.setVisibility(visibility)
    }
}