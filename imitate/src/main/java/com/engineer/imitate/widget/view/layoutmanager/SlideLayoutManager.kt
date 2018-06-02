package com.engineer.imitate.widget.view.layoutmanager

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class SlideLayoutManager : RecyclerView.LayoutManager {

    private val mRecyclerView: RecyclerView
    private val mItemTouchHelper: ItemTouchHelper

    private val mOnTouchListener: MyTouchListener = MyTouchListener()


    constructor(mRecyclerView: RecyclerView, mItemTouchHelper: ItemTouchHelper) {
        this.mItemTouchHelper = checkIsNull(mItemTouchHelper)
        this.mRecyclerView = checkIsNull(mRecyclerView)
    }

    private fun <T> checkIsNull(t: T?): T {
        if (t == null) {
            throw NullPointerException()
        }
        return t
    }

    inner class MyTouchListener:View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

            val childViewHolder = mRecyclerView.getChildViewHolder(v)

            if (event?.action == MotionEvent.ACTION_DOWN) {
                mItemTouchHelper.startSwipe(childViewHolder)
            }
            return false
        }

    }




    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        detachAndScrapAttachedViews(recycler)
        val itemCount = itemCount
        if (itemCount > ItemConfig.DEFAULT_SHOW_ITEM) {
            for (position in ItemConfig.DEFAULT_SHOW_ITEM downTo 0) {
                val view = recycler!!.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 5,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 5 + getDecoratedMeasuredHeight(view))

                if (position == ItemConfig.DEFAULT_SHOW_ITEM) {
                    view.scaleX = 1 - (position - 1) * ItemConfig.DEFAULT_SCALE
                    view.scaleY = 1 - (position - 1) * ItemConfig.DEFAULT_SCALE
                    view.translationY = ((position - 1) * view.measuredHeight / ItemConfig.DEFAULT_TRANSLATE_Y).toFloat()
                } else if (position > 0) {
                    view.scaleX = 1 - position * ItemConfig.DEFAULT_SCALE
                    view.scaleY = 1 - position * ItemConfig.DEFAULT_SCALE
                    view.translationY = (position * view.measuredHeight / ItemConfig.DEFAULT_TRANSLATE_Y).toFloat()
                } else {
                    view.setOnTouchListener(mOnTouchListener)
                }
            }
        } else {
            for (position in itemCount - 1 downTo 0) {
                val view = recycler!!.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, 0, 0)
                val widthSpace = width - getDecoratedMeasuredWidth(view)
                val heightSpace = height - getDecoratedMeasuredHeight(view)
                layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 5,
                        widthSpace / 2 + getDecoratedMeasuredWidth(view),
                        heightSpace / 5 + getDecoratedMeasuredHeight(view))

                if (position > 0) {
                    view.scaleX = 1 - position * ItemConfig.DEFAULT_SCALE
                    view.scaleY = 1 - position * ItemConfig.DEFAULT_SCALE
                    view.translationY = (position * view.measuredHeight / ItemConfig.DEFAULT_TRANSLATE_Y).toFloat()
                } else {
                    view.setOnTouchListener(mOnTouchListener)
                }
            }
        }
    }

}