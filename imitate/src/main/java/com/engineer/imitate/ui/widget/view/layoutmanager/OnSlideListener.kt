package com.engineer.imitate.ui.widget.view.layoutmanager

interface OnSlideListener<T> {
     fun onSliding(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, ratio: Float, direction: Int)

     fun onSlided(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, t: T, direction: Int )

     fun onClear()
}