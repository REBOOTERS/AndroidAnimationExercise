package com.engineer.imitate.ui.widget.headsup

import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

class Pudding : LifecycleObserver {
    private lateinit var choco: Choco

    private var windowManager: WindowManager? = null

    // after create
    fun show() {
        windowManager?.also {
            try {
                it.addView(choco, initLayoutParameter())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // time over dismiss
        choco.postDelayed({
            if (choco.enableInfiniteDuration) {
                return@postDelayed
            }
            choco.hide()
        }, Choco.DISPLAY_TIME)

        // click dismiss
        choco.setOnClickListener {
            choco.hide()
        }

    }

    // window manager must associate activity's lifecycle
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        // this owner is your activity instance
        choco.hide(true)
        owner.lifecycle.removeObserver(this)
        if (puddingMapX.containsKey(owner.toString())) {
            puddingMapX.remove(owner.toString())
        }
    }

    private fun initLayoutParameter(): WindowManager.LayoutParams {
        // init layout params
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            0, 0,
            PixelFormat.TRANSPARENT
        )
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.TOP

        layoutParams.flags =
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or // 不获取焦点，以便于在弹出的时候 下层界面仍然可以进行操作
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR // 确保你的内容不会被装饰物(如状态栏)掩盖.
        // popWindow的层级为 TYPE_APPLICATION_PANEL
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL

        return layoutParams
    }

    // must invoke first
    private fun setActivity(activity: AppCompatActivity, block: Choco.() -> Unit, view: View) {
        activityWeakReference = WeakReference(activity)
        choco = Choco(activity)
        windowManager = activity.windowManager

        activity.lifecycle.addObserver(this)

        // 指定使用者的高阶函数named dsl 配置 choco属性
        choco.apply(block)

        Log.e("zyq", "view height " + view.measuredHeight)
        choco.addView(view)
    }

    companion object {

        @JvmStatic
        private fun log(e: String) {
            Log.e(this::class.java.simpleName, "${this} $e")
        }

        private var activityWeakReference: WeakReference<AppCompatActivity>? = null

        // each Activity hold itself pudding list
        private val puddingMapX: MutableMap<String, Pudding> = mutableMapOf()

        @JvmStatic
        fun create(activity: AppCompatActivity, view: View, block: Choco.() -> Unit): Pudding {
            val pudding = Pudding()
            pudding.setActivity(activity, block, view)

            Handler(Looper.getMainLooper()).post {
                puddingMapX[activity.toString()]?.choco?.let {
                    if (it.isAttachedToWindow) {
                        ViewCompat.animate(it).alpha(0F).withEndAction {
                            if (it.isAttachedToWindow) {
                                activity.windowManager.removeViewImmediate(it)
                            }
                        }
                    }
                }
                puddingMapX[activity.toString()] = pudding
            }

            return pudding
        }
    }
}