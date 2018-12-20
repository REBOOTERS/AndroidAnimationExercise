package com.engineer.imitate.activity

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.engineer.imitate.R
import com.engineer.imitate.util.dp2px
import kotlinx.android.synthetic.main.activity_custom_scrolling.*


val TAG = "CustomScrollingActivity"

class CustomScrollingActivity : AppCompatActivity() {

    private lateinit var context: Context

    private var toolbarHeight = 0.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_custom_scrolling)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        app_bar.addOnOffsetChangedListener(AppBarLayout
                .OnOffsetChangedListener { p0, p1 ->
                    Log.e(TAG, "p1==$p1")
                    Log.e(TAG, "p2==" + p0.minimumHeightForVisibleOverlappingContent)
                    Log.e(TAG, "p3==" + p0.totalScrollRange)
                    val percent = (100.0f * Math.abs(p1) / p0.totalScrollRange) / 100.0f

                    Log.e(TAG, "percent==" + (100 * Math.abs(p1) / p0.totalScrollRange) / 100.0f)

                    if (percent >= 0.0f) {
                        toolbar_up.visibility = View.VISIBLE
                    } else {
                        toolbar_up.visibility = View.GONE
                    }

//                    toolbar_up.scaleY = percent
//                    toolbar_up.pivotY = 0.0f

                    if (percent > 0.5f) {
                        toolbar_up.translationY = -toolbarHeight + (percent * 2) * toolbarHeight
                    } else {
                        toolbar_up.translationY = -2 * percent * toolbarHeight
                    }


                })
    }

    override fun onResume() {
        super.onResume()
        toolbarHeight = context.dp2px(48.0f)
        Log.e(TAG, "toolbarHeight=$toolbarHeight")
        toolbar_up.translationY = -toolbarHeight
    }
}
