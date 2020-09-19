package com.engineer.imitate.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Path
import android.graphics.PathMeasure
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.engineer.imitate.R

/**
 * @author Rookie
 * @since 06-24-2020
 *
 * 小鱼干领取动画
 */
@SuppressLint("LogNotTimber")
object AnimDelegate {


    fun apply(context: Context?, source: View?, target: View?, container: ViewGroup?) {
        if (context == null || source === null || target == null || container == null) {
            return
        }
        val img = ImageView(context)
        val params = ViewGroup.LayoutParams(28.dp, 28.dp)
        img.setImageResource(R.drawable.totoro)
        container.addView(img, params)

        val position = FloatArray(2)

        val room = IntArray(2)
        container.getLocationInWindow(room)

        val from = IntArray(2)
        source.getLocationInWindow(from)

        val to = IntArray(2)
        target.getLocationInWindow(to)


        Log.e("zyq", ": room ${room[0]},${room[1]}")
        Log.e("zyq", ": from ${from[0]},${from[1]}")
        Log.e("zyq", ": to   ${to[0]},${to[1]}")
        Log.e("zyq", ": target ${target.measuredWidth},${target.measuredHeight}")
        Log.e("zyq", ": img    ${img.measuredWidth},${img.measuredHeight}")

        val startX = from[0].toFloat() - room[0] + source.width / 2 - 8.dp
        val startY = from[1].toFloat() - room[1] + source.height / 2 - 16.dp

        val endX = to[0].toFloat() - room[0] + 14.dp
        val endY = to[1].toFloat() - room[1] + 14.dp

        val path = Path()
        path.moveTo(startX, startY)
        path.lineTo(endX, endY)

        val pathMeasure = PathMeasure(path, false)
        val anim = ValueAnimator.ofFloat(0f, pathMeasure.length)
        anim.addUpdateListener {
            val value = it.animatedValue as Float
            pathMeasure.getPosTan(value, position, null)
            Log.e("zyq", ": ${position[0]},${position[1]}")
            img.translationX = position[0]
            img.translationY = position[1]
        }
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                img.animate().alpha(0f).withEndAction {
                    container.removeView(img)
                }.start()
            }
        })
        anim.duration = 800
        anim.start()
    }
}