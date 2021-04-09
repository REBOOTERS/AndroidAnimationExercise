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


class LargeImageAdapter(private var datas: List<String>) :
    RecyclerView.Adapter<LargeImageAdapter.MyHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.large_image_item, parent, false)

        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        val index = position % Routes.IMAGES.size
        val res = Routes.IMAGES[index]

        Glide.with(mContext).load(res).into(holder.imageView)
        holder.tv.text = "$position"
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image)
        var tv: TextView = itemView.findViewById(R.id.item_tv)
    }
}