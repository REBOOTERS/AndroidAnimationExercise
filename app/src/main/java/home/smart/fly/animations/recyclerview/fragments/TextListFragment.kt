package home.smart.fly.animations.recyclerview.fragments

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import home.smart.fly.animations.R
import home.smart.fly.animations.recyclerview.DataFactory

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 07-23-2019
 */
class TextListFragment : BaseListFragment() {

    override fun loadDatas(): ArrayList<String> {
        return DataFactory.initStringData()
    }

    override fun getCustomAdapter(): RecyclerView.Adapter<*> {
        return MyAdapter(datas)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_text_list_recycler_view
    }


    inner class MyAdapter(private var datas: ArrayList<String>) : RecyclerView.Adapter<MyAdapter.MyHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val textView = TextView(parent.context)
            return MyHolder(textView)
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.tv.text = datas[position]
        }

        inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tv = view as TextView
        }
    }
}