package com.engineer.imitate.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.engineer.imitate.util.dp2px

/**
 * @author rookie
 * @since 12-10-2019
 */
class DecorationGod(val context: Context) : RecyclerView.ItemDecoration() {

    private val dp8 = context.dp2px(8f).toInt()
    private val dp4 = context.dp2px(4f).toInt()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        if (pos == 0) {
            outRect.left = dp8
        }

        outRect.right = dp8
        outRect.top = dp4
        outRect.bottom = dp4
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        c.drawColor(Color.GREEN)
//        c.rotate(2f)
        c.skew(0.5f, 0f)
    }

}