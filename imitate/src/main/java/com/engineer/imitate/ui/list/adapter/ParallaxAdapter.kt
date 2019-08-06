package com.engineer.imitate.ui.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.util.TimeUtils
import androidx.recyclerview.widget.RecyclerView
import com.engineer.imitate.R

class ParallaxAdapter(private val backImages: List<Int>, private val frontImages: List<Int>) : RecyclerView.Adapter<TestHolder>() {

    override fun onBindViewHolder(holder: TestHolder, position: Int) {
        holder.ivImage.setImageResource(backImages[position])
        holder.ivImage2.setImageResource(frontImages[position])
        TimeUtils.HUNDRED_DAY_FIELD_LEN
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestHolder =
            TestHolder(LayoutInflater.from(parent.context).inflate(R.layout.parallax_item, parent, false))


    override fun getItemCount(): Int = backImages.size
}


class TestHolder(root: View) : RecyclerView.ViewHolder(root) {
    var ivImage: AppCompatImageView = root.findViewById(R.id.ivImage)
    var ivImage2: AppCompatImageView = root.findViewById(R.id.ivImage2)
}