package com.engineer.imitate.ui.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.engineer.imitate.R
import com.engineer.imitate.Routes
import com.engineer.imitate.util.dp2px


class ImageAdapter(private var datas: List<String>) :
    RecyclerView.Adapter<ImageAdapter.MyHolder>() {

    private lateinit var mContext: Context
    private var option: RequestOptions = RequestOptions.circleCropTransform()

    private fun dp2dx(context: Context, value: Int): Float {
        return context.dp2px(value.toFloat())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.h_image_item, parent, false)

        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
//        Glide.with(mContext).load(datas[position])
//                .apply(option)
//                .into(holder.imageView)
        val index = position % Routes.IMAGES.size
        val res = Routes.IMAGES[index]

        Glide.with(mContext).load(res).into(holder.imageView)
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(com.engineer.imitate.R.id.image)
    }
}