package com.engineer.imitate.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.core.widget.NestedScrollView
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import com.engineer.imitate.R
import com.engineer.imitate.util.dp2px
import com.engineer.imitate.widget.slidepane.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_pure.*


const val tag = "PureActivity"
class PureActivity : AppCompatActivity() {

    private lateinit var context: Context


    private var toolbarHeight = 0.0f

    private var screenH = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_pure)


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

        sliding_layout.addPanelSlideListener(object :SlidingUpPanelLayout.PanelSlideListener{
            override fun onPanelSlide(panel: View?, slideOffset: Float) {
                nested_scrollview.scrollBy(0,(1000*slideOffset).toInt())
            }

            override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?) {
            }

        })


        nested_scrollview.setOnScrollChangeListener(object :NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(p0: NestedScrollView?, p1: Int, p2: Int, p3: Int, p4: Int) {
                Log.e(tag,"p2=====$p2")



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
            }

        })
    }


    override fun onResume() {
        super.onResume()
        toolbarHeight = context.dp2px(56.0f)
        Log.e(TAG, "toolbarHeight=$toolbarHeight")


        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        screenH = dm.heightPixels

        Log.e(TAG, "screenH  =$screenH")
        Log.e(TAG, "statusbar=" + getStatusBarHeight())

    }

    private fun getStatusBarHeight(): Int {
        val resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourcesId)
    }
}
