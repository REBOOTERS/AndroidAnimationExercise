package com.engineer.imitate.ui.widget.view.layoutmanager

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper

/**
 * Created by 钉某人
 * github: https://github.com/DingMouRen
 * email: naildingmouren@gmail.com
 */


class ItemTouchHelperCallback<T> : ItemTouchHelper.Callback {

    private val adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>
    private var dataList: MutableList<T>? = null
    private var mListener: OnSlideListener<T>? = null

    constructor(adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>, dataList: MutableList<T>) {
        this.adapter = adapter
        this.dataList = dataList

    }

    constructor(adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>, dataList: MutableList<T>, listener: OnSlideListener<T>) {
        this.adapter = adapter
        this.dataList = dataList
        this.mListener = listener
    }

    private fun <T> checkIsNull(t: T?): T {
        if (t == null) {
            throw NullPointerException()
        }
        return t
    }

    fun setOnSlideListener(mListener: OnSlideListener<T>) {
        this.mListener = mListener
    }

    override fun getMovementFlags(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Int {
        val dragFlags = 0
        var slideFlags = 0
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is SlideLayoutManager) {
            slideFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        }
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, slideFlags)
    }

    override fun onMove(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, target: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
        viewHolder.itemView.setOnTouchListener(null)
        val layoutPosition = viewHolder.layoutPosition
        val remove = dataList!!.removeAt(layoutPosition)
        adapter.notifyDataSetChanged()
        if (mListener != null) {
            mListener!!.onSlided(viewHolder, remove, if (direction == ItemTouchHelper.LEFT) ItemConfig.SLIDED_LEFT else ItemConfig.SLIDED_RIGHT)
        }
        if (adapter.itemCount == 0) {
            if (mListener != null) {
                mListener!!.onClear()
            }
        }
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            var ratio = dX / getThreshold(recyclerView, viewHolder)
            if (ratio > 1) {
                ratio = 1f
            } else if (ratio < -1) {
                ratio = -1f
            }
            itemView.rotation = ratio * ItemConfig.DEFAULT_ROTATE_DEGREE
            val childCount = recyclerView.childCount
            if (childCount > ItemConfig.DEFAULT_SHOW_ITEM) {
                for (position in 1 until childCount - 1) {
                    val index = childCount - position - 1
                    val view = recyclerView.getChildAt(position)
                    view.scaleX = 1 - index * ItemConfig.DEFAULT_SCALE + Math.abs(ratio) * ItemConfig.DEFAULT_SCALE
                    view.scaleY = 1 - index * ItemConfig.DEFAULT_SCALE + Math.abs(ratio) * ItemConfig.DEFAULT_SCALE
                    view.translationY = (index - Math.abs(ratio)) * itemView.measuredHeight / ItemConfig.DEFAULT_TRANSLATE_Y
                }
            } else {
                for (position in 0 until childCount - 1) {
                    val index = childCount - position - 1
                    val view = recyclerView.getChildAt(position)
                    view.scaleX = 1 - index * ItemConfig.DEFAULT_SCALE + Math.abs(ratio) * ItemConfig.DEFAULT_SCALE
                    view.scaleY = 1 - index * ItemConfig.DEFAULT_SCALE + Math.abs(ratio) * ItemConfig.DEFAULT_SCALE
                    view.translationY = (index - Math.abs(ratio)) * itemView.measuredHeight / ItemConfig.DEFAULT_TRANSLATE_Y
                }
            }
            if (mListener != null) {
                if (ratio != 0f) {
                    mListener!!.onSliding(viewHolder, ratio, if (ratio < 0) ItemConfig.SLIDING_LEFT else ItemConfig.SLIDING_RIGHT)
                } else {
                    mListener!!.onSliding(viewHolder, ratio, ItemConfig.SLIDING_NONE)
                }
            }
        }
    }

    override fun clearView(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.rotation = 0f
    }

    private fun getThreshold(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder): Float {
        return recyclerView.width * getSwipeThreshold(viewHolder)
    }

}
