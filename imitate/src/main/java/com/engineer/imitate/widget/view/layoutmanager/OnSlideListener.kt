package com.engineer.imitate.widget.view.layoutmanager

import android.support.v7.widget.RecyclerView

interface OnSlideListener<T> {
     fun onSliding(viewHolder: RecyclerView.ViewHolder, ratio: Float, direction: Int)

     fun onSlided(viewHolder: RecyclerView.ViewHolder, t: T, direction: Int )

     fun onClear()
}