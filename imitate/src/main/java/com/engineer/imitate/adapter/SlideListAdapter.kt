package com.engineer.imitate.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.engineer.imitate.R

class SlideListAdapter(private var datas: List<String>) : RecyclerView.Adapter<SlideListAdapter.MyHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        mContext = parent.context
        return MyHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.slide_card_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Glide.with(mContext).load(datas[position]).into(holder.imageView)
    }


    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var imageView: ImageView

        init {
            if (itemView != null) {
                imageView = itemView.findViewById(R.id.image)
            }
        }
    }
}