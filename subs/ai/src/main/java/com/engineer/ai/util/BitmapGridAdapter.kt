package com.engineer.ai.util

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.engineer.ai.R

class BitmapGridAdapter(private val bitmaps: List<Bitmap>) :
    RecyclerView.Adapter<BitmapGridAdapter.BitmapViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitmapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bitmap, parent, false)
        return BitmapViewHolder(view)
    }

    override fun onBindViewHolder(holder: BitmapViewHolder, position: Int) {
        holder.bind(bitmaps[position])
    }

    override fun getItemCount(): Int = bitmaps.size

    class BitmapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(bitmap: Bitmap) {
            imageView.setImageBitmap(bitmap)
        }
    }
}