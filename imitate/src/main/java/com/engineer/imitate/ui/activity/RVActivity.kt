package com.engineer.imitate.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.engineer.imitate.Routes
import com.engineer.imitate.databinding.ActivityRVBinding
import com.engineer.imitate.util.dp

class RVActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityRVBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRVBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.rvList.layoutManager = LinearLayoutManager(this)
        val adapter = SimpleAdapter(provideData())
        viewBinding.rvList.adapter = adapter
        viewBinding.rvList.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        val snapHelper = getSnapHelper()
        snapHelper.attachToRecyclerView(viewBinding.rvList)

        viewBinding.rvList.postDelayed({
            viewBinding.refreshLayout.autoRefresh()
        }, 1000)
    }

    private fun getSnapHelper(): SnapHelper {
        val array = arrayListOf(PagerSnapHelper(), LinearSnapHelper())
        return array.random()
    }

    private fun provideData(): List<Int> {
        val pics = ArrayList<Int>()
        pics.addAll(Routes.IMAGES)
        return pics
    }
}

private class SimpleAdapter(val items: List<Int>) : RecyclerView.Adapter<SimpleAdapter.MyHolder>() {

    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frameLayout = view as FrameLayout
        val imageView: ImageView = frameLayout.getChildAt(0) as ImageView
        val textView: TextView = frameLayout.getChildAt(1) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val ctx = parent.context
        val iv = ImageView(ctx)
        iv.scaleType = ImageView.ScaleType.CENTER
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        )
        val tv = TextView(ctx)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
        tv.setTextColor(Color.RED)
        params.gravity = Gravity.CENTER
        val frame = FrameLayout(ctx)
        frame.layoutParams = params
        frame.setBackgroundColor(Color.BLACK)
        frame.addView(iv, params)
        params.gravity = Gravity.BOTTOM
        params.bottomMargin = 16.dp
        frame.addView(tv, params)
        return MyHolder(frame)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.imageView.setImageResource(items[position])
        holder.textView.text = "pos: $position"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}