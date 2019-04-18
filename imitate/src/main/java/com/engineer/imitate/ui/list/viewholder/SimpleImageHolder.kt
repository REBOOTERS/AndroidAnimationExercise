package com.engineer.imitate.ui.list.viewholder

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.bumptech.glide.request.RequestOptions
import com.engineer.imitate.R
import com.zhihu.android.sugaradapter.Layout
import com.zhihu.android.sugaradapter.SugarHolder

/**
 *
 * @author: zhuyongging
 * @since: 2019-04-18
 */
@Layout(R.layout.simple_image_item)
class SimpleImageHolder(view: View) : SugarHolder<String>(view) {

    private var mImageView: ImageView

    init {
        mImageView = view.findViewById(R.id.image)
    }

    override fun onBindData(data: String) {
        var options: RequestOptions = RequestOptions.circleCropTransform()

//        when (adapterPosition) {
//            in 0..2 -> options = RequestOptions.circleCropTransform()
//            in 3..5 -> options = RequestOptions.centerCropTransform()
//            in 6..9 -> options = RequestOptions.centerInsideTransform()
//        }

//        TransformationUtils.circleCrop(Glide.get(context).bitmapPool, null, 10, 10)


        Glide.with(context)
                .applyDefaultRequestOptions(options)
                .load(data).into(mImageView)
    }
}