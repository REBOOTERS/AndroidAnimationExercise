package home.smart.fly.animations.ui.activity

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import home.smart.fly.animations.R
import home.smart.fly.animations.utils.DpConvert
import kotlinx.android.synthetic.main.activity_fake_douban_movie.*

const val TAG = "FakeDoubanMovie"

class FakeDoubanMovie : AppCompatActivity() {

    private var show: Boolean = false
    private lateinit var mContext: Context;

    private var realH = 0

    private var guideLine = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
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

        nested_scrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { p0, p1, p2, p3, p4 ->
            Log.e(TAG, "p1==$p1")
            Log.e(TAG, "p2==$p2")
            Log.e(TAG, "p3==$p3")
            Log.e(TAG, "p4==$p4")

            val location = IntArray(2)
            fake.getLocationOnScreen(location)

            val y = location[1]

            Log.e(TAG, "y     ==" + location[1])
            Log.e(TAG, "h     ==" + DpConvert.dip2px(mContext, 48.0f))
            Log.e(TAG, "realH ==" + realH)

            var delta = Math.abs(realH - DpConvert.dip2px(mContext, 48.0f))


            if (y < delta) {
                guideLine  = realH -y
                guideline1.setGuidelineEnd(realH - y)
            } else {
                guideLine = DpConvert.dip2px(mContext, 48.0f)
                guideline1.setGuidelineEnd(DpConvert.dip2px(mContext, 48.0f))
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

                if (guideLine > DpConvert.dip2px(mContext, 48.0f)) {

                } else {
                    value.start()
                }


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
