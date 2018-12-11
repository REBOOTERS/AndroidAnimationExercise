package home.smart.fly.animations.ui.activity

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import home.smart.fly.animations.R
import kotlinx.android.synthetic.main.activity_fake_douban_movie.*

const val TAG = "FakeDoubanMovie"

class FakeDoubanMovie : AppCompatActivity() {

    private var show: Boolean = false

    private var realH = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_douban_movie)
        setSupportActionBar(toolbar)
        setTitle("测试")
        initView()
    }

    override fun onResume() {
        super.onResume()
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val screenH = dm.heightPixels;


        realH = screenH
    }

    private fun initView() {

        nested_scrollview.setOnScrollChangeListener(object :NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(p0: NestedScrollView?, p1: Int, p2: Int, p3: Int, p4: Int) {
                Log.e(TAG, "p1==$p1")
                Log.e(TAG, "p2==$p2")
                Log.e(TAG, "p3==$p3")
                Log.e(TAG, "p4==$p4")
            }
        })


        bottom.setOnClickListener {


            if (!show) {
                val value = ValueAnimator.ofInt(realH - appbar.measuredHeight - getStatusBarHeight())
                value.addUpdateListener {
                    val result = it.animatedValue
                    Log.e("anim", "result ===" + result)
                    guideline1.setGuidelineEnd(result as Int)
                }
                value.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        show = true
                    }

                })
                value.start()
            } else {
                val value = ValueAnimator.ofInt(realH - appbar.measuredHeight - getStatusBarHeight(), bottom.measuredHeight)
                value.addUpdateListener {
                    val result = it.animatedValue
                    Log.e("anim", "result ===" + result)
                    guideline1.setGuidelineEnd(result as Int)
                }
                value.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        show = false
                    }

                })
                value.start()
            }


        }
    }


    private fun getStatusBarHeight(): Int {
        val resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourcesId)
    }
}
