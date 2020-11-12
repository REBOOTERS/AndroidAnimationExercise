package com.engineer.imitate.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.engineer.imitate.R
import com.engineer.imitate.util.dp
import com.engineer.imitate.util.hide
import com.engineer.imitate.util.show
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_c_l.*
import java.util.concurrent.TimeUnit

class CLActivity : AppCompatActivity() {
    private val TAG = "CLActivity"
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c_l)

        val current = layout_container.translationY
        layout_container.translationY = current + 46.dp
        val y1 = comment.translationY
        val x = layout_container.translationX
        comment.translationY = y1 + 46.dp
        layout_container.translationX = x + 20.dp
        layout_container.alpha = 0f

        Log.e(TAG,"46.dp = ${46.dp}, 20.dp = ${20.dp}")
        var open = false

        comment.setOnClickListener {
            var y1 = layout_container.translationY
            var y2 = comment.translationY
            val x = layout_container.translationX

            Log.e(TAG,"y1=$y1,y2=$y2,x=$x")

            if (open) {
                open = false
                val anim1 = ObjectAnimator.ofFloat(layout_container, "translationY", y1, y1 + 46.dp)
                    .setDuration(200)
                val anim2 =
                    ObjectAnimator.ofFloat(comment, "translationY", y2, y2 + 46.dp).setDuration(200)
                val anim3 =
                    ObjectAnimator.ofFloat(layout_container, "alpha", 1f, 0f).setDuration(200)

                val anim4 = ObjectAnimator.ofFloat(layout_container, "translationX", x, x + 20.dp)
                    .setDuration(10)
                anim1.start()
                anim2.start()
                anim3.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        anim4.start()
                    }
                })
                anim3.start()
//                anim4.start()
            } else {
                open = true
                val anim1 = ObjectAnimator.ofFloat(layout_container, "translationY", y1, y1 - 46.dp)
                    .setDuration(200)
                val anim2 =
                    ObjectAnimator.ofFloat(comment, "translationY", y2, y2 - 46.dp).setDuration(200)
                val anim3 =
                    ObjectAnimator.ofFloat(layout_container, "alpha", 0f, 1f).setDuration(200)
                val anim4 = ObjectAnimator.ofFloat(layout_container, "translationX", x, x - 20.dp)
                    .setDuration(200)
                anim3.startDelay = 50
                anim4.startDelay = 50
                anim1.start()
                anim2.start()
                anim3.start()
                anim4.start()
            }

            Log.e(TAG, "x = ${comment.x}, y = ${comment.y}")
            Log.e(
                TAG,
                "translationX = ${comment.translationX}, translationY = ${comment.translationY}"
            )

            Log.e(
                TAG,
                "left = ${comment.left},top = ${comment.top}, right = ${comment.right},bottom  = ${comment.bottom}"
            )


            // 获得状态栏高度
            val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
            val statusBarHeight = resources.getDimensionPixelSize(resourceId)

            val maxY = resources.displayMetrics.heightPixels - 56.dp - statusBarHeight
            Log.e(TAG, "maxY = $maxY")

//            val params = layout_container.layoutParams
//            params.height = 0
//            layout_container.layoutParams = params
//
//            val valueAnimator = ValueAnimator.ofInt(36.dp)
//            valueAnimator.addUpdateListener {
//                val value: Int = it.animatedValue as Int
//                val fraction = it.animatedFraction
////            layout_container.alpha = it.animatedFraction
//                Log.e(TAG, "value = $value, fraction = $fraction")
//                params.height = value
//                layout_container.layoutParams = params
//            }
//            valueAnimator.startDelay = 1000
//            valueAnimator.start()
        }


    }

    private fun show() {

        disposable = Observable.intervalRange(1, 3, 0, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                comment.text = "$it"

                if (it.toInt() == 3) {
                    layout_container.show()
                    hideLatter()
                }
            }
    }

    private fun hideLatter() {
        disposable = Observable.intervalRange(1, 3, 1, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                comment.text = "-$it"
                if (it.toInt() == 3) {
                    layout_container.hide()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}