package com.engineer.phenix.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.engineer.phenix.R
import com.engineer.phenix.bean.ImageBean
import com.engineer.phenix.internal.glide.ImageLoader
import com.engineer.phenix.internal.glide.engine.SimpleFileTarget
import java.io.File
import java.lang.Exception

/**
 *
 * @author: rookie
 * @date: 2018-07-20 18:29
 * @version V1.0
 */

class ViewPagerAdapter:PagerAdapter {

    private lateinit var context: Context
    private lateinit var items:List<ImageBean>

    constructor(items:List<ImageBean>) {
        this.items = items
    }

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

        val item = items[position]
        val originalUrl = item.imageUrl
        var thumbnailUrl = item.thumbnailUrl

        var cacheFile = ImageLoader.getGlideCacheFile(context, originalUrl)
        if (cacheFile != null && cacheFile.exists()) {
            Glide.with(context).load(cacheFile).downloadOnly(object :SimpleFileTarget(){

                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    super.onResourceReady(resource, transition)
                    imageView.setImage(ImageSource.uri(Uri.fromFile(resource.absoluteFile)))
                    progress.visibility =View.GONE
                }
            })
        } else {
            Glide.with(context).load(thumbnailUrl).downloadOnly(object :SimpleFileTarget(){
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    progress.visibility = View.VISIBLE
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    progress.visibility = View.GONE

                }
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                    super.onResourceReady(resource, transition)
                    imageView.setImage(ImageSource.uri(Uri.fromFile(resource.absoluteFile)))
                    progress.visibility =View.GONE
                }

            })
        }

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
        try {
            container.removeView(`object` as View?)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            ImageLoader.clearMemory(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }







}