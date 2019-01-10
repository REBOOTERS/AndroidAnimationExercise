package home.smart.fly.animations.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import home.smart.fly.animations.R
import home.smart.fly.animations.widget.NestedScrollView
import kotlinx.android.synthetic.main.activity_pull_to_scale.*
import kotlinx.android.synthetic.main.content_pull_to_scale.*

class PullToScaleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_to_scale)
        setSupportActionBar(toolbar)

        nested_scrollview.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            /**
             * Called when the scroll position of a view changes.
             *
             * @param v The view whose scroll position has changed.
             * @param scrollX Current horizontal scroll origin.
             * @param scrollY Current vertical scroll origin.
             * @param oldScrollX Previous horizontal scroll origin.
             * @param oldScrollY Previous vertical scroll origin.
             */
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {

                Log.e("TAG", "scrolly===$scrollY")
                if (scrollY >= 0) {
                    val height = image.measuredHeight
                    if (height != content.paddingTop) {
                        var params = image.layoutParams
                        params.height = content.paddingTop
                        image.layoutParams = params
                    }
                    val isOver = scrollY >= height
                    if (isOver) {
                        image.visibility = View.GONE
                    } else {
                        image.visibility = View.VISIBLE
                    }
                    image.scrollTo(0, scrollY/3)
                } else {
                    image.scrollTo(0, 0)
                    val params = image.layoutParams
                    params.height = content.paddingTop - scrollY
                    image.layoutParams = params
                }
            }

        })
    }
}
