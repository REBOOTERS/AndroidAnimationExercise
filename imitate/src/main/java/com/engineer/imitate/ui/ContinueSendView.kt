package com.engineer.imitate.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.vx_continue_send_view.view.*

class ContinueSendView(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    companion object {
        private const val COUNT = 10
        private const val TOTAL_TIME = 10 * 1000
    }

    val ldEnd = MutableLiveData<Boolean>()
    val giftCount = MutableLiveData<Int>()
    // 当前总共要送出的礼物个数
    private var currentCount = 0
    // 上一次倒计时结束前准备要送出的礼物个数
    private var lastCount = 0;

    private lateinit var view: View
    private lateinit var timeAnimator: ValueAnimator

    init {
        initView()
        initTimeAnimator()
    }

    private fun initTimeAnimator() {
        timeAnimator = ValueAnimator()
        timeAnimator.setIntValues(TOTAL_TIME, 0)
        timeAnimator.duration = TOTAL_TIME.toLong()
        timeAnimator.interpolator = LinearInterpolator()
        timeAnimator.addUpdateListener {
            val time = it.animatedValue as Int
            currentCount = COUNT - (time/1000)
            Log.e("zyq","time is $currentCount")
            updateProgress(time)
            updateTime(time / 1000)

            if (time == 0) {
                ldEnd.postValue(true)
                giftCount.value = currentCount + lastCount
            }
        }
        timeAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                updateProgress(TOTAL_TIME)
                updateTime(TOTAL_TIME / 1000)
            }

            override fun onAnimationEnd(animation: Animator?) {

            }
        })
    }

    private fun initView() {
        view = LayoutInflater.from(context).inflate(R.layout.vx_continue_send_view, this, true)
        view.progress.max = TOTAL_TIME
    }

    private fun updateTime(time: Int) {
        view.text_time.text = context.getString(R.string.vx_gift_continue_send_time, time)
    }

    private fun updateProgress(progress: Int) {
        view.progress.progress = progress
    }

    //重新倒计时
    fun restartCountdown() {
        timeAnimator.cancel()

        timeAnimator.start()
    }
    // 离开屏幕大的时候，强制关闭
    fun forceStop() {
        timeAnimator.cancel()
        ldEnd.value = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.e("zyq","onDetachedFromWindow")
        timeAnimator.cancel()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.e("zyq", "action down")
                // 当前要送出的礼物数量暂存一下
                lastCount = currentCount
                restartCountdown()
                return true
            }
            MotionEvent.ACTION_UP -> {
                Log.e("zyq", "action up")
                giftCount.value = currentCount + lastCount
                timeAnimator.cancel()
                ldEnd.value = true
                return true
            }
            else -> {
                return super.onTouchEvent(event)
            }
        }
    }

}