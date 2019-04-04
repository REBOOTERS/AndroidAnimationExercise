package com.engineer.imitate.widget.view.layoutmanager

import androidx.recyclerview.widget.RecyclerView

interface OnSlideListener<T> {
     fun onSliding(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, ratio: Float, direction: Int)

     fun onSlided(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, t: T, direction: Int )

     fun onClear()
}