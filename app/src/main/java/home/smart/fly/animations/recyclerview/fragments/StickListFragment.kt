package home.smart.fly.animations.recyclerview.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import home.smart.fly.animations.R
import home.smart.fly.animations.recyclerview.DataFactory
import home.smart.fly.animations.recyclerview.bean.StickBean
import home.smart.fly.animations.recyclerview.customitemdecoration.sticky.StickyItemDecoration

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 07-23-2019
 */
class StickListFragment : BaseListFragment<StickBean>() {
    override fun loadDatas(): ArrayList<StickBean> {
        return DataFactory.initStickData()
    }


    override fun getCustomAdapter(): RecyclerView.Adapter<*> {
        return MyAdapter(datas)
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_text_list_recycler_view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView().addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        getRecyclerView().addItemDecoration(StickyItemDecoration())
    }


    inner class MyAdapter(private var datas: ArrayList<StickBean>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val type_noraml = 0
        private val type_stick = 11

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == type_stick) {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_bold_textview_item, parent, false)
                return StickHoler(view)
            } else {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.simple_textview_item, parent, false)
                return MyHolder(view)
            }
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        override fun getItemViewType(position: Int): Int {
            if (position % 6 == 0) {
                return type_stick
            } else {
                return type_noraml
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is StickHoler) {
                holder.tv.text = datas[position].content
            } else if (holder is MyHolder) {
                holder.tv.text = datas[position].content
            }
        }

        inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
            var tv: TextView = view.findViewById(R.id.text)

            init {
                view.tag = false
            }
        }

        inner class StickHoler(view: View) : RecyclerView.ViewHolder(view) {
            var tv: TextView = view.findViewById(R.id.text)

            init {
                view.tag = true
            }
        }
    }
}