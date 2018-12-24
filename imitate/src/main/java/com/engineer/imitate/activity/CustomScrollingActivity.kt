package com.engineer.imitate.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.engineer.imitate.R
import com.engineer.imitate.util.dp2px
import com.engineer.imitate.widget.slidepane.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_custom_scrolling.*


val TAG = "CustomScrollingActivity"

class CustomScrollingActivity : AppCompatActivity() {

    private lateinit var context: Context

    private var lock = false  // 互斥锁

    private var toolbarHeight = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_custom_scrolling)
        fab.setOnClickListener { view ->

            if (toolbar_up.translationY < 0) {
                toolbar_up.animate().translationYBy(toolbarHeight).start()
            } else {
                toolbar_up.animate().translationYBy(-toolbarHeight).start()
            }
        }

        drag_bar.setOnClickListener {
            if (toolbar_up.translationY < 0) {
                toolbar_up.animate().translationYBy(toolbarHeight).start()
            } else {
                toolbar_up.animate().translationYBy(-toolbarHeight).start()
            }
        }

        sliding_layout.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            Log.e(TAG, "left==$left")
            Log.e(TAG, "top==$top")
            Log.e(TAG, "right==$right")
            Log.e(TAG, "bottom==$bottom")
        }

        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                Log.e(TAG, "slideOffset==$slideOffset")

                lock = true

                if (lock) {
                    toolbar_up.translationY = toolbarHeight * slideOffset - toolbarHeight
                }
            }

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
                Log.e(TAG, "previousState==$previousState")
                Log.e(TAG, "newState     ==$newState")
            }

        })



        app_bar.addOnOffsetChangedListener(AppBarLayout
                .OnOffsetChangedListener { p0, p1 ->
                    Log.e(TAG, "p1              ==$p1")
                    Log.e(TAG, "totalScrollRange==" + p0.totalScrollRange)
                    var percent = (100.0f * Math.abs(p1 * 2) / p0.totalScrollRange) / 100.0f

                    Log.e(TAG, "percent==$percent")

                    if (percent > 1) {
                        percent = 1.0f
                    }

                    if (Math.abs(p1) >= 775) {
                        val p = (100.0f * (Math.abs(p1) - 775)) / (p0.totalScrollRange - 775) / 100
                        Log.e(TAG, "y ===" + p)


                        sliding_layout.smoothSlideTo(p, 0)
                    } else {
                        sliding_layout.smoothSlideTo(0f, 0)
//                        sliding_layout.panelHeight = context.dp2px(54.0f).toInt()
                    }

                    sliding_layout.requestLayout()

                    lock = false
                    if (!lock) {
                        toolbar_up.translationY = toolbarHeight * percent - toolbarHeight
                    }
                })

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (sliding_layout.panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                }
            }


        })
    }

    override fun onResume() {
        super.onResume()
        toolbarHeight = context.dp2px(56.0f)
        Log.e(TAG, "toolbarHeight=$toolbarHeight")
        toolbar_up.translationY = -toolbarHeight
        toolbar_up.visibility = View.VISIBLE
        Log.e(TAG, "yyyy====" + toolbar_up.translationY)

    }
}
