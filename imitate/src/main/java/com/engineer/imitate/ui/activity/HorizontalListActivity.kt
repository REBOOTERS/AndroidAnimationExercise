package com.engineer.imitate.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.engineer.imitate.R
import com.engineer.imitate.util.dp2px
import com.list.rados.fast_list.FastListAdapter
import com.list.rados.fast_list.bind
import kotlinx.android.synthetic.main.activity_horizontal_list.*
import kotlinx.android.synthetic.main.view_item_h.view.*

class HorizontalListActivity : AppCompatActivity() {

    private lateinit var adapter: FastListAdapter<String>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_list)
        val datas = initData()


        adapter = list.bind(datas, R.layout.view_item_h) { value: String ->
            setOnClickListener {
                val pos = datas.indexOf(value)
                Toast.makeText(context, "pos $pos", Toast.LENGTH_SHORT).show()
//                list.smoothScrollToPosition(pos + 1)
                val xy = IntArray(2)
                getLocationOnScreen(xy)
                val dest = xy[0] - dp2px(24f)
                Log.e(TAG, "dest==: $dest")
                if (dest > 0) {
                    list.smoothScrollBy(dest.toInt(), 0)
                }
            }
            desc.text = value
            path.text = "pos " + datas.indexOf(value)

        }.layoutManager(LinearLayoutManager(this, RecyclerView.HORIZONTAL, false))
    }

    private fun initData(): ArrayList<String> {
        val datas = ArrayList<String>()
        for (i in 0..5) {
            datas.add("item $i")
        }
        return datas
    }
}
