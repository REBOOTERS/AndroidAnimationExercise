package com.engineer.imitate.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.util.SparseArray
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.engineer.imitate.R
import com.engineer.imitate.databinding.ActivityCLBinding
import com.engineer.imitate.ui.BulletView
import com.engineer.imitate.util.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class CLActivity : AppCompatActivity() {
    private val TAG = "CLActivity"
    var disposable: Disposable? = null
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var viewBinding: ActivityCLBinding

    private var countDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCLBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        var out = true
        viewBinding.toggle.setOnClickListener {
            val sparseArray = SparseArray<String>()
            sparseArray.put(1, "a")
            sparseArray.put(2, "b")
            if (out) {
                out = false
                viewBinding.magicCard.animate().translationX(0f).setStartDelay(0).withEndAction {

                        countDisposable =
                            Observable.just(0).delay(10, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    viewBinding.magicCard.animate().translationX(240.dp.toFloat())
                                        .withEndAction { out = true }.start()
                                }

                    }.start()
            }
        }
        viewBinding.closeCard.setOnClickListener {
            countDisposable?.dispose()
            viewBinding.magicCard.hide()
            viewBinding.magicCard.animate().translationX(240.dp.toFloat()).withEndAction {
                    viewBinding.magicCard.show()
                    out = true
                }.start()
        }
        viewBinding.magicCardTv.text = "\u3000\u3000\u3000" + getString(R.string.content)
        viewBinding.magicCardTv.setTypeface(null, Typeface.BOLD)
        viewBinding.magicCardTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)


        viewBinding.layoutContainer.translationY = 46.dp.toFloat()
        viewBinding.comment.translationY = 46.dp.toFloat()
        viewBinding.layoutContainer.translationX = 20.dp.toFloat()
        viewBinding.layoutContainer.alpha = 0f

        Log.e(TAG, "46.dp = ${46.dp}, 20.dp = ${20.dp}")
        var open = false

        viewBinding.comment.setOnClickListener {
            var y1 = viewBinding.layoutContainer.translationY
            var y2 = viewBinding.comment.translationY
            val x = viewBinding.layoutContainer.translationX

            Log.e(TAG, "y1=$y1,y2=$y2,x=$x")

            if (open) {

                updateView(viewBinding.comment, 200.dp)
                open = false
                val anim1 =
                    ObjectAnimator.ofFloat(viewBinding.layoutContainer, "translationY", y1, y1 + 46.dp).setDuration(200)
                val anim2 = ObjectAnimator.ofFloat(viewBinding.comment, "translationY", y2, y2 + 46.dp).setDuration(200)
                val anim3 = ObjectAnimator.ofFloat(viewBinding.layoutContainer, "alpha", 1f, 0f).setDuration(200)

                val anim4 =
                    ObjectAnimator.ofFloat(viewBinding.layoutContainer, "translationX", x, x + 20.dp).setDuration(10)
                anim1.start()
                anim2.start()
                anim3.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        anim4.start()
                    }
                })
                anim3.start()
//                anim4.start()
            } else {
                updateView(viewBinding.comment, 144.dp)
                open = true
                val anim1 =
                    ObjectAnimator.ofFloat(viewBinding.layoutContainer, "translationY", y1, y1 - 46.dp).setDuration(200)
                val anim2 = ObjectAnimator.ofFloat(viewBinding.comment, "translationY", y2, y2 - 46.dp).setDuration(200)
                val anim3 = ObjectAnimator.ofFloat(viewBinding.layoutContainer, "alpha", 0f, 1f).setDuration(200)
                val anim4 =
                    ObjectAnimator.ofFloat(viewBinding.layoutContainer, "translationX", x, x - 20.dp).setDuration(200)
                anim3.startDelay = 50
                anim4.startDelay = 150
                anim1.start()
                anim2.start()
                anim3.start()
                anim4.start()
            }

            Log.e(TAG, "x = ${viewBinding.comment.x}, y = ${viewBinding.comment.y}")
            Log.e(
                TAG,
                "translationX = ${viewBinding.comment.translationX}, translationY = ${viewBinding.comment.translationY}"
            )

            Log.e(
                TAG,
                "left = ${viewBinding.comment.left},top = ${viewBinding.comment.top}, right = ${viewBinding.comment.right},bottom  = ${viewBinding.comment.bottom}"
            )


//            // 获得状态栏高度
//            val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
//            val statusBarHeight = resources.getDimensionPixelSize(resourceId)
//
//            val maxY = resources.displayMetrics.heightPixels - 56.dp - statusBarHeight
//            Log.e(TAG, "maxY = $maxY")
        }

        var count = 0
        var continueCount = 0

//        Observable.interval(4, TimeUnit.SECONDS)
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnNext {
//                Log.e(TAG,"IT == $it")
//                viewBinding.comment.performClick()
//            }
//            .subscribe().add(compositeDisposable)

        var d: Disposable? = null

        viewBinding.sendGift.setOnClickListener {
            ++count
            if (d != null && d!!.isDisposed.not()) {
                d!!.dispose()
            }

            d = Observable.just(0).delay(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewBinding.continueSendView.ldEnd.value = false
                    viewBinding.continueSendView.restartCountdown()
                }
            viewBinding.countTime.text = "送礼 $count 次,连击 $continueCount 次"
        }
//        viewBinding.continueSendView.setOnClickListener {
//            if (d != null && d!!.isDisposed.not()) {
//                d!!.dispose()
//            }
//            ++continueCount
//            d = Observable.just(0).delay(100, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    viewBinding.continueSendView.restartCountdown()
//                }
//            viewBinding.countTime.text = "送礼 $count 次,连击 $continueCount 次"
//        }
        viewBinding.continueSendView.ldEnd.observe(this, Observer {
            if (it) {
                viewBinding.continueSendView.invisible()
                viewBinding.sendGift.show()
            } else {
                viewBinding.continueSendView.show()
                viewBinding.sendGift.invisible()
            }
        })

        viewBinding.continueSendView.giftCount.observe(this) {
            toastShort("发送礼物 $it 个")
            Log.e("ContinueSendView", "发送礼物 $it 个")
        }
        viewBinding.continueSendView.setOnProgressUpdateListener(object : BulletView.OnProgressUpdateListener {
            var lastValue = -1
            override fun update(count: Int) {
                if (count != lastValue) {
                    viewBinding.countTime.text = "count $count"
                }
                lastValue = count
            }

        })

        viewBinding.continueSendView.invisible()

        viewBinding.forceHide.setOnClickListener {
            viewBinding.continueSendView.forceStop()
        }

        val html = getText(R.string.what_the_html)
        viewBinding.htmlTv.text = html

        viewBinding.testButton.setOnClickListener {
            if (viewBinding.testButton.visibility == View.VISIBLE) {
                viewBinding.testButton.invisible()
            } else {
                viewBinding.testButton.show()
            }
        }
        //过滤换行符
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            source.toString().replace("\n", " ")
        }
//        viewBinding.input.filters = arrayOf(InputFilter.LengthFilter(81), filter)
        viewBinding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        viewBinding.input.setOnEditorActionListener { v, actionId, event ->
            actionId == EditorInfo.IME_ACTION_DONE && (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)
        }
        viewBinding.input.setHorizontallyScrolling(false)
        viewBinding.input.maxLines = Int.MAX_VALUE

    }

    private fun updateView(view: View, height: Int) {
        val param = view.layoutParams
        param.height = height
        view.layoutParams = param
    }

    private fun show() {

        disposable =
            Observable.intervalRange(1, 3, 0, 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    viewBinding.comment.text = "$it"

                    if (it.toInt() == 3) {
                        viewBinding.layoutContainer.show()
                        hideLatter()
                    }
                }
    }

    private fun hideLatter() {
        disposable =
            Observable.intervalRange(1, 3, 1, 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    viewBinding.comment.text = "-$it"
                    if (it.toInt() == 3) {
                        viewBinding.layoutContainer.hide()
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        compositeDisposable.dispose()
    }
}