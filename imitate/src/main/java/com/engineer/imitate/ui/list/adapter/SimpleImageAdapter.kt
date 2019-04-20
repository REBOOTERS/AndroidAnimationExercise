package com.engineer.imitate.ui.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.engineer.imitate.R

class SimpleImageAdapter(private var datas: List<String>) : RecyclerView.Adapter<SimpleImageAdapter.MyHolder>() {

    private lateinit var mContext: Context
    private var option: RequestOptions = RequestOptions.circleCropTransform()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        mContext = parent.context
        return MyHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.simple_image_item, parent, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Glide.with(mContext).load(datas[position])
                .apply(option)
                .into(holder.imageView)
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image)
    }
}