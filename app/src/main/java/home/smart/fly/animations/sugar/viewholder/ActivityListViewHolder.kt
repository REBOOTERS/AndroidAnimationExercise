package home.smart.fly.animations.sugar.viewholder

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.zhihu.android.sugaradapter.Id
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder
import home.smart.fly.animations.R

/**
 *
 * @authro: Rookie
 * @since: 2019-04-14
 */

@Layout(R.layout.demo_info_item)
class ActivityListViewHolder(view: View) : SugarHolder<Pair<String, String>>(view) {

    @Id(R.id.title)
    lateinit var title: TextView

    @Id(R.id.desc)
    lateinit var desc: TextView

    @Id(R.id.itemshell)
    lateinit var itemshell: RelativeLayout


    override fun onBindData(data: Pair<String, String>) {
        title.text = data.first
        desc.text = data.second
    }


}