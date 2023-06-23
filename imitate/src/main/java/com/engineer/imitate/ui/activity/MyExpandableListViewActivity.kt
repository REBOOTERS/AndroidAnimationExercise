package com.engineer.imitate.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView.OnGroupCollapseListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.engineer.imitate.R
import com.engineer.imitate.databinding.ActivityExpandableListViewBinding
import com.engineer.imitate.util.dp
import com.engineer.imitate.util.toastShort
import kotlin.random.Random

class MyExpandableListViewActivity : AppCompatActivity() {

    private lateinit var mContext: Context
    private lateinit var viewBinding: ActivityExpandableListViewBinding

    private var lastGroupPosition = -1
    private val groupData = arrayOf(
        "同事", "老师", "朋友", "同事", "老师",
        "朋友", "同事", "老师", "朋友", "老师", "朋友", "同事", "老师", "朋友"
    )
    private val childData = arrayOf(
        arrayOf("小小", "小明", "吴老师", "肖老师", "雯雯", "哔哔", "饭饭", "流浪"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "柳老师", "小小", "小明", "饭饭", "流浪"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "雯雯", "哔哔", "嘻嘻"),
        arrayOf("小小", "小明", "吴老师", "肖老师", "雯雯", "哔哔", "饭饭", "流浪"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "柳老师", "小小", "小明", "饭饭", "流浪"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "雯雯", "哔哔", "嘻嘻"),
        arrayOf("小小", "小明", "吴老师", "肖老师", "雯雯", "哔哔", "饭饭", "流浪"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "柳老师", "小小", "小明", "饭饭", "流浪"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "雯雯", "哔哔", "嘻嘻"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "柳老师", "小小", "小明", "饭饭", "流浪"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "雯雯", "哔哔", "嘻嘻"),
        arrayOf("小小", "小明", "吴老师", "肖老师", "雯雯", "哔哔", "饭饭", "流浪"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "柳老师", "小小", "小明", "饭饭", "流浪"),
        arrayOf("李老师", "张老师", "吴老师", "肖老师", "雯雯", "哔哔", "嘻嘻")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityExpandableListViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        mContext = this
        //给ExpandableListAdapter设置适配器---自定义适配器需要继承BaseExpandableListAdapter()实现其中的方法
        val myExpandableListAdapter = MyExpandableListAdapter()

//        viewBinding.expandableLv.divider = getDrawable(R.drawable.line)
//        viewBinding.expandableLv.setChildDivider(getDrawable(R.drawable.line))
        //设置适配器
        viewBinding.expandableLv.setAdapter(myExpandableListAdapter)
        //去掉group默认的箭头
//        viewBinding.expandableLv.setGroupIndicator(null)
        //设置组可拉伸的监听器,拉伸时会调用其中的onGroupExpand()方法
        viewBinding.expandableLv.setOnGroupExpandListener {
            /**
             * 实现打开只能打开一个组的功能,打开一个组,已将打开的组会自动收缩
             */
            /**
             * 实现打开只能打开一个组的功能,打开一个组,已将打开的组会自动收缩
             */
//            if (it !== lastGroupPosition) {
//                viewBinding.expandableLv.collapseGroup(lastGroupPosition)
//            }
//            lastGroupPosition = it
        }
        //设置组收缩的监听器,收缩时会调用其中的onGroupCollapse()方法
        viewBinding.expandableLv.setOnGroupCollapseListener(OnGroupCollapseListener { })

//        viewBinding.expandableLv.setSelectedChild(1, 0, true)

//        viewBinding.expandableLv.smoothScrollToPosition(12)
        val pos: Int = (Random.nextFloat() * groupData.size).toInt()
        viewBinding.expandableLv.setSelection(pos)
        val count = viewBinding.expandableLv.count
        for (i in 0 until count) {
            viewBinding.expandableLv.expandGroup(i)
        }
        val image = ImageView(this)
        image.setImageResource(R.drawable.totoro)
        val container = LinearLayout(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100.dp)
        container.addView(image, params)
        viewBinding.expandableLv.addHeaderView(container)
        toastShort("expand group $pos")
    }


    internal inner class MyExpandableListAdapter : BaseExpandableListAdapter() {
        /**
         * 得到组的数量
         */
        override fun getGroupCount(): Int {
            return groupData.size
        }

        /**
         * 得到每个组的元素的数量
         */
        override fun getChildrenCount(groupPosition: Int): Int {
            return 1
        }

        /**
         * 获得组的对象
         */
        override fun getGroup(groupPosition: Int): Any {
            return groupData[groupPosition]
        }

        /**
         * 获得子对象
         */
        override fun getChild(groupPosition: Int, childPosition: Int): Any {
            return childData[groupPosition][childPosition]
        }

        /**
         * 得到组id
         */
        override fun getGroupId(groupPosition: Int): Long {
            return groupPosition.toLong()
        }

        /**
         * 得到子id
         */
        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return childPosition.toLong()
        }

        /**
         * 表示数据是否稳定,对监听事件有影响
         */
        override fun hasStableIds(): Boolean {
            return true
        }

        /**
         * 确定一个组的展示视图--groupPosition表示的当前需要展示的组的索引
         */
        override fun getGroupView(
            groupPosition: Int, isExpanded: Boolean,
            convertView: View?, parent: ViewGroup
        ): View? {

            val inflater = LayoutInflater.from(mContext)
            val view = inflater.inflate(R.layout.expandable_group_item, parent, false)
            val group_text = view.findViewById(R.id.group_text) as TextView
            group_text.text = groupData[groupPosition]
            return view
        }

        /**
         * 确定一个组的一个子的展示视图--groupPostion表示当前组的索引,childPosition表示的是需要展示的子的索引
         */
        override fun getChildView(
            groupPosition: Int, childPosition: Int,
            isLastChild: Boolean, convertView: View?, parent: ViewGroup
        ): View? {
            var view = convertView
            var holder: ChildViewHolder?
            if (view == null) {
                holder = ChildViewHolder()
                val inflater = LayoutInflater.from(mContext)

            } else {
                holder = view.tag as ChildViewHolder
            }
            val datas = childData[groupPosition].toList()


            return view
        }

        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
            return false
        }

        inner class GroupViewHolder {
            private lateinit var group_text: TextView
        }

        inner class ChildViewHolder {

        }

    }

}
