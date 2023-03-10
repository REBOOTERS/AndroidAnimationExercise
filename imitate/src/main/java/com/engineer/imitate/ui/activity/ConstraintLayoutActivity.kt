package com.engineer.imitate.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.engineer.imitate.databinding.ActivityConstraintLayoutBinding
import com.engineer.imitate.ui.list.adapter.LargeImageAdapter
import com.engineer.imitate.ui.widget.more.DZStickyNavLayouts
import com.engineer.imitate.util.hide
import com.engineer.imitate.util.show

class ConstraintLayoutActivity : AppCompatActivity() {
    val tag = "ConstraintLayout"
    private lateinit var viewBinding: ActivityConstraintLayoutBinding
    private lateinit var layoutParams: FrameLayout.LayoutParams
    private val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewBinding.rootContent?.let {
                val displayRect = Rect()
                it.getWindowVisibleDisplayFrame(displayRect)
                Log.e(tag, "${displayRect.top}")
                Log.e(tag, "${displayRect.right}")
                Log.e(tag, "${displayRect.bottom}")
                Log.e(tag, "${displayRect.left}")

//                scrollView.scrollBy(0, -displayRect.bottom)
//                layoutParams.height = displayRect.bottom
//                layoutParams.bottomMargin = 1000
                it.layoutParams = layoutParams

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityConstraintLayoutBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.recyclerView2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        viewBinding.recyclerView2.adapter = LargeImageAdapter(getList())
        viewBinding.headHomeLayout.setOnStartActivity(object : DZStickyNavLayouts.OnStartActivityListener {
            override fun onStart() {
                Toast.makeText(this@ConstraintLayoutActivity, "bingo", Toast.LENGTH_SHORT).show()
            }
        })

        var toggle = false
        viewBinding.anim.setOnClickListener {
            if (toggle) {
                viewBinding.imageAlpha.alpha = 0.0f
            } else {
                viewBinding.imageAlpha.alpha = 1.0f
            }
            toggle = !toggle
        }
        viewBinding.rotateAnim.setOnClickListener {
            viewBinding.rotateAnim.rotation = 0f
            rotateAnim(viewBinding.rotateAnim)
        }
        viewBinding.button2.setOnClickListener {
            if (viewBinding.button1.visibility == View.VISIBLE) {
                viewBinding.button1.hide()
            } else {
                viewBinding.button1.show()
            }
        }
    }

    private var rotation: ViewPropertyAnimator? = null
    private fun rotateAnim(view: View) {
        rotation = view.animate().rotation(90f).setStartDelay(400).setDuration(600)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    rotation = view.animate().rotation(0f).setStartDelay(400).setDuration(600)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                rotation =
                                    view.animate().rotation(90f).setStartDelay(400).setDuration(600)
                                rotation?.start()
                            }
                        })
                    rotation?.start()
                }
            })
        rotation?.start()

    }

    // <editor-fold defaultstate="collapsed" desc="prepare datas">
    private fun getList(): MutableList<String> {
        val datas = ArrayList<String>()
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        datas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555581149653&di=5912dd2fe4db77ce303569b3e8f34d7b&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201406%2F08%2F20140608161225_VYVEV.jpeg")
        return datas
    }
    // </editor-fold>

    override fun onResume() {
        super.onResume()
        layoutParams = viewBinding.rootContent.layoutParams as FrameLayout.LayoutParams
        viewBinding.rootContent.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun onPause() {
        super.onPause()
        viewBinding.rootContent.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }
}
