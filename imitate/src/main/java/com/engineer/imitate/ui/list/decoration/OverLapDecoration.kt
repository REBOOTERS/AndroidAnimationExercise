package com.engineer.imitate.ui.list.decoration

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author: zhuyongging
 * @since: 2019-04-18
 */
class OverLapDecoration(context: Context?) : RecyclerView.ItemDecoration() {

    val overlapLimit = 20
    var overlapWidth = 0

    init {
        val displayMetrics = context!!.resources.displayMetrics
        overlapWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, -12f, displayMetrics).toInt()
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        super.getItemOffsets(outRect, view, parent, state)
        //-----get current position of item
        val itemPosition = parent.getChildAdapterPosition(view)

        val isReverseLayout = (parent.layoutManager as LinearLayoutManager).reverseLayout

        //-----avoid first item decoration else it will go of the screen
        if (itemPosition == 0) {
            return
        } else {

            if (isReverseLayout) {
                when {
                    itemPosition <= overlapLimit -> outRect.set(0, 0, overlapWidth, 0)
                    /*itemPosition == overlapLimit -> {
                        outRect.set(overlapWidth * -1, 0, 0, 0)
                    }*/
                    else -> outRect.set(0, 0, 0, 0)
                }
            } else {
                //-----apply decoration
                when {
                    itemPosition <= overlapLimit -> outRect.set(overlapWidth, 0, 0, 0)
                    /*itemPosition == overlapLimit -> {
                    outRect.set(overlapWidth * -1, 0, 0, 0)
                }*/
                    else -> outRect.set(0, 0, 0, 0)
                }
            }
        }
    }
}