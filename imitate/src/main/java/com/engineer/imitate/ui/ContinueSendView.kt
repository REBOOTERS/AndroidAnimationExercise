package com.engineer.imitate.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import com.engineer.imitate.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.vx_continue_send_view.view.*
import java.util.concurrent.TimeUnit

class ContinueSendView(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    companion object {
        private const val COUNT = 100
        private const val TOTAL_TIME = 10 * 1000
    }

    private var vibrator: Vibrator? = null

    val ldEnd = MutableLiveData<Boolean>()
    val giftCount = MutableLiveData<Int>()

    // 当前总共要送出的礼物个数
    private var currentCount = 0

    // 上一次倒计时结束前准备要送出的礼物个数
    private var lastCount = 0

    // 最毒可以送出的礼物数量，动画执行的时候，等于这个数量时直接
    private var maxCount: Long = Int.MAX_VALUE.toLong()

    // 手指是否按在屏幕上进行连击
    private var inTouch = false

    // 一次连击的网络请求是否已经发出去了，包括手指抬起和 10 秒倒计时计数
    private var hasSend = false

    private lateinit var view: View
    private lateinit var timeAnimator: ValueAnimator

    init {
        initView()
        initTimeAnimator()
        vibrator = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?
    }


    private val time = 125L
    private var giftCountDisposable: Disposable? = null
    private fun startReCount() {
        if (giftCountDisposable != null) {
            Log.e("ContinueSendView", "giftCountDisposable not null")
            return
        }
        Log.e("ContinueSendView", "giftCountDisposable is null")
        giftCountDisposable =
            Observable.intervalRange(
                2, Int.MAX_VALUE.toLong(),
                time, time, TimeUnit.MILLISECONDS
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it <= maxCount) {
                        val result = it.toInt() + lastCount
                        updateListener?.update(result)
                        vibrator?.vibrate(40)
                        Log.e("ContinueSendView", "it ==$result, ldEnd=${ldEnd.value}")
                        currentCount = result
                        if (ldEnd.value == true) {
                            stopRecount()
                        }
                    } else {
                        stopRecount()
                    }

                }

    }

    private fun stopRecount() {
        giftCountDisposable?.dispose()
        giftCountDisposable = null
    }


    private fun initTimeAnimator() {
        timeAnimator = ValueAnimator()
        timeAnimator.setIntValues(TOTAL_TIME, 0)
        timeAnimator.duration = TOTAL_TIME.toLong()
        timeAnimator.interpolator = LinearInterpolator()
        timeAnimator.addUpdateListener {
            val time = it.animatedValue as Int
            updateProgress(time)
            updateTime(time / 1000)
            if (time == 0) {
                ldEnd.value = true
                if (inTouch and hasSend.not()) {
                    // 这种情况说明，手指在连续按，刚好倒计时走完了
                    giftCount.value = currentCount
                    hasSend = true
                }
                currentCount = 0
                lastCount = 0
                stopRecount()
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
        ldEnd.value = false
        hasSend = false
        timeAnimator.cancel()
        timeAnimator.start()
    }

    fun setMaxCount(max: Long) {
        maxCount = max
    }

    // 离开屏幕大的时候，强制关闭
    fun forceStop() {
//        timeAnimator.cancel()
        ldEnd.value = true
        lastCount = 0
        currentCount = 0
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.e("ContinueSendView", "onDetachedFromWindow")
        timeAnimator.cancel()
        stopRecount()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                vibrator?.vibrate(40)
                Log.e("ContinueSendView", "action down : ${System.currentTimeMillis()}")
                // 当前要送出的礼物数量暂存一下
                inTouch = true
                lastCount = currentCount
                restartCountdown()
                startReCount()
                return true

            }
            MotionEvent.ACTION_UP -> {
                Log.e("ContinueSendView", "action up : ${System.currentTimeMillis()}")
                inTouch = false
                if (hasSend.not() && currentCount > 0) {
                    giftCount.value = currentCount
                    hasSend = true
                }
                stopRecount()
                if (currentCount == lastCount) {
                    Log.e("ContinueSendView", "单击？？$currentCount,$lastCount")
                    currentCount += 1
                    updateListener?.update(currentCount)
                    giftCount.value = currentCount
                    hasSend = true
                }
                return true
            }
            else -> {
                return super.onTouchEvent(event)
            }
        }
    }

    interface OnProgressUpdateListener {
        fun update(count: Int)
    }

    private var updateListener: OnProgressUpdateListener? = null

    fun setOnProgressUpdateListener(l: OnProgressUpdateListener) {
        this.updateListener = l
    }
}