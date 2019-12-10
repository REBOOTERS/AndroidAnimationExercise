package com.engineer.imitate.ui.widget

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 12-10-2019
 */
class DecorationGod (val context: Context):RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
    }

}