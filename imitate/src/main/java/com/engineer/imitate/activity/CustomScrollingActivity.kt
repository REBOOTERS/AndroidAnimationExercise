package com.engineer.imitate.activity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import com.engineer.imitate.R
import com.engineer.imitate.util.dp2px
import com.engineer.imitate.widget.slidepane.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_custom_scrolling.*


val TAG = "CustomScrollingActivity"

class CustomScrollingActivity : AppCompatActivity() {

    private lateinit var context: Context


    private var toolbarHeight = 0.0f

    private var screenH = 0

    private var app_bar_h = 0


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


        sliding_layout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                Log.e(TAG, "slideOffset==$slideOffset")

                val location = IntArray(2)
                fake_tab.getLocationInWindow(location)
                Log.e(TAG, "location[1] ====" + location[1])

//                app_bar.scrollTo(0,-1000)

//                app_bar.translationY = app_bar_h*slideOffset

                toolbar_up.translationY = toolbarHeight * slideOffset - toolbarHeight
            }

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
//                Log.e(TAG, "previousState==$previousState")
//                Log.e(TAG, "newState     ==$newState")
            }

        })



//        val anim = ValueAnimator.ofFloat(0f,1000f)
//        anim.duration = 8000
//        anim.addUpdateListener {
//            app_bar.translationY = - (it.animatedValue as Float)
//        }
//        anim.start()

        app_bar.addOnOffsetChangedListener(AppBarLayout
                .OnOffsetChangedListener { p0, p1 ->

                    app_bar_h = p1

                    var percent = (100.0f * Math.abs(p1 * 2) / p0.totalScrollRange) / 100.0f

//                    Log.e(TAG, "percent==$percent")

                    if (percent > 1) {
                        percent = 1.0f
                    }

                    toolbar_up.translationY = toolbarHeight * percent - toolbarHeight

                    val location = IntArray(2)
                    fake_tab.getLocationInWindow(location)


                    val h = screenH - fake_tab.measuredHeight - toolbarHeight - getStatusBarHeight()

                    Log.e(TAG, "location[1] ==" + location[1])


                    if (location[1] > 0) {
                        val my = screenH - fake_tab.measuredHeight
                        if (location[1] < my) {
                            var y = my - location[1]

                            Log.e(TAG, "y    ===$y")

                            if (y > h) {
                                y = h.toInt()
                            }
//                            dragView.translationY = -y.toFloat()


                            var p = 100f * (h - y) / h / 100f
                            Log.e(TAG, "p====$p")

                            if (p == 0f) {
                                sliding_layout.anchorPoint = 1.0f
                                sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                            } else {
                                sliding_layout.anchorPoint = 1 - p
                                sliding_layout.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
                            }


                        } else {
                            dragView.translationY = 0f
                            sliding_layout.anchorPoint = 0.0f
                            sliding_layout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
                        }
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

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        screenH = dm.heightPixels

        Log.e(TAG, "screenH  =" + screenH)
        Log.e(TAG, "statusbar=" + getStatusBarHeight())

    }

    private fun getStatusBarHeight(): Int {
        val resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourcesId)
    }
}
