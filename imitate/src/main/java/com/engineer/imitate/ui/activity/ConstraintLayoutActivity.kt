package com.engineer.imitate.ui.activity

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.activity_constraint_layout.*

class ConstraintLayoutActivity : AppCompatActivity() {
    val tag = "ConstraintLayoutActivity"

    private lateinit var layoutParams: FrameLayout.LayoutParams
    private val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            root_content?.let {
                val displayRect = Rect()
                it.getWindowVisibleDisplayFrame(displayRect)
                Log.e(tag, "${displayRect.top}")
                Log.e(tag, "${displayRect.right}")
                Log.e(tag, "${displayRect.bottom}")
                Log.e(tag, "${displayRect.left}")

//                scrollView.scrollBy(0, -displayRect.bottom)
//                layoutParams.height = displayRect.bottom
                layoutParams.bottomMargin = 1000
                it.layoutParams = layoutParams

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_layout)
    }

    override fun onResume() {
        super.onResume()
        layoutParams = root_content.layoutParams as FrameLayout.LayoutParams
        root_content.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun onPause() {
        super.onPause()
        root_content.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }
}
