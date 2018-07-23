package com.engineer.phenix.ui

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.engineer.phenix.R
import com.engineer.phenix.bean.ImageBean

/**
 *
 * @author: zhuyongging
 * @date: 2018-07-20 18:29
 * @version V1.0
 */

class ViewPagerAdapter:PagerAdapter {

    private lateinit var context: Context
    private lateinit var items:List<ImageBean>

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return items.size
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        context = container.context
        var itemView = View.inflate(context,R.layout.item_photoview,null)
        var imageView: SubsamplingScaleImageView = itemView.findViewById(R.id.image)
        var progress: ProgressBar = itemView.findViewById(R.id.progress_view)

        return super.instantiateItem(container, position)
    }


    constructor(items:List<ImageBean>)




}