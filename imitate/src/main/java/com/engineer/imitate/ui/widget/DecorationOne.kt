package com.engineer.imitate.ui.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.engineer.imitate.util.dp2px

/**
 * @author rookie
 * @since 12-10-2019
 */
class DecorationOne(val context: Context) : RecyclerView.ItemDecoration() {
    val TAG = "DecorationOne"

    private val dp8 = context.dp2px(8f).toInt()
    private val dp14 = context.dp2px(14f).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        when (pos) {
            0 -> {
                outRect.left = dp14
            }
            5 -> {
                outRect.left = dp8
                outRect.right = dp14
            }
            else -> {
                outRect.left = dp8
            }
        }
    }
}
