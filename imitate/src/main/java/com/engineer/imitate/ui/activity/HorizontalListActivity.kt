package com.engineer.imitate.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.engineer.imitate.R
import com.engineer.imitate.databinding.ActivityHorizontalListBinding
import com.engineer.imitate.ui.widget.DecorationGod
import com.engineer.imitate.ui.widget.DecorationOne
import com.engineer.imitate.util.FastListAdapter
import com.engineer.imitate.util.bind
import com.engineer.imitate.util.dp2px
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer

class HorizontalListActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityHorizontalListBinding
    private lateinit var adapter: FastListAdapter<String>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        intent.getParcelableExtra<TransformationLayout.Params>("TransformationParams")?.let {
            onTransformationEndContainer(it)
        }
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHorizontalListBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_horizontal_list)
        val datas = initData()

        adapter = viewBinding.list00.bind(datas, R.layout.view_item_h_card) { value: String, _: Int ->
            setOnClickListener {
                val pos = datas.indexOf(value)
                Toast.makeText(context, "pos $pos", Toast.LENGTH_SHORT).show()
                viewBinding.list.smoothScrollToPosition(pos + 1)
            }
            val desc = findViewById<TextView>(R.id.desc)
            val path = findViewById<TextView>(R.id.path)
            desc.text = value
            path.text = "pos " + datas.indexOf(value)

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))

        adapter = viewBinding.list0.bind(datas, R.layout.view_item_h_card) { value: String, _: Int ->
            setOnClickListener {
                val pos = datas.indexOf(value)
                Toast.makeText(context, "pos $pos", Toast.LENGTH_SHORT).show()
                viewBinding.list.smoothScrollToPosition(pos + 1)
            }
            val desc = findViewById<TextView>(R.id.desc)
            val path = findViewById<TextView>(R.id.path)
            desc.text = value
            path.text = "pos " + datas.indexOf(value)

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))



        adapter = viewBinding.list.bind(datas, R.layout.view_item_h_square) { value: String, _: Int ->
            setOnClickListener {
                val pos = datas.indexOf(value)
                Toast.makeText(context, "pos $pos", Toast.LENGTH_SHORT).show()
                viewBinding.list.smoothScrollToPosition(pos + 1)
            }
            val desc = findViewById<TextView>(R.id.desc)
            val path = findViewById<TextView>(R.id.path)
            desc.text = value
            path.text = "pos " + datas.indexOf(value)

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))
        viewBinding.list.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))


        viewBinding.list2.bind(datas, R.layout.view_item_h) { value: String, _: Int ->
            setOnClickListener {
                val pos = datas.indexOf(value)
                Toast.makeText(context, "pos $pos", Toast.LENGTH_SHORT).show()
                val xy = IntArray(2)
                getLocationOnScreen(xy)
                val dest = xy[0] - dp2px(24f)
                Log.e(TAG, "dest==: $dest")
                if (dest > 0) {
                    viewBinding.list2.smoothScrollBy(dest.toInt(), 0)
                }
            }
            val desc = findViewById<TextView>(R.id.desc)
            val path = findViewById<TextView>(R.id.path)
            desc.text = value
            path.text = "pos " + datas.indexOf(value)

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))
        viewBinding.list2.addItemDecoration(DecorationOne(this))
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(viewBinding.list2)


        viewBinding.list3.bind(datas, R.layout.view_item_h_image) { value: String, _: Int ->
            val desc = findViewById<TextView>(R.id.desc)
            val path = findViewById<TextView>(R.id.path)
            desc.text = value

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))
        viewBinding.list3.addItemDecoration(DecorationGod(this))
    }

    override fun onResume() {
        super.onResume()
        val result = true
    }

    private fun initData(): ArrayList<String> {
        val datas = ArrayList<String>()
        for (i in 0 until 6) {
            datas.add("item $i")
        }
        return datas
    }
}
