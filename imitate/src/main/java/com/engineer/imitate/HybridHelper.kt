package com.engineer.imitate

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.DialogTitle
import android.util.Log
import android.webkit.JavascriptInterface
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter

/**
 *
 * @author: rookie
 * @date: 2018-07-29 10:09
 * @version V1.0
 */

class HybridHelper(private var context: Context) {

    private var listener: OnItemClickListener? = null

    @JavascriptInterface
    fun go(path: String) {
        go(path, "")
    }

    @JavascriptInterface
    fun go(path: String, title: String) {
        val fragment: Fragment =
            ARouter.getInstance().build(path).navigation(context, ARouterCallback()) as Fragment
        listener?.onClick(fragment, title)
    }


    interface OnItemClickListener {
        fun onClick(fragment: Fragment, title: String)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private class ARouterCallback : NavCallback() {

        private val TAG: String = "ARouter"

        override fun onFound(postcard: Postcard?) {
            Log.e(TAG, "onFound: " + postcard!!.toString())
        }

        override fun onLost(postcard: Postcard?) {
            Log.e(TAG, "onLost: " + postcard!!.toString())
        }

        override fun onArrival(postcard: Postcard) {
            Log.e(TAG, "onArrival: " + postcard.toString())
        }

        override fun onInterrupt(postcard: Postcard?) {
            Log.e(TAG, "onInterrupt: " + postcard!!.toString())
        }

    }
}