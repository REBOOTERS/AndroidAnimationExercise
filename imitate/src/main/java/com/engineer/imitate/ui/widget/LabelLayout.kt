package com.engineer.imitate.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.engineer.imitate.R
import com.engineer.imitate.util.dp
import kotlinx.android.synthetic.main.label_layout_container.view.*


class LabelLayoutProvider {
    companion object {
        fun provideLabelLayout(context: Context, labels: Array<String>): View {
            val toggle = LabelLayoutContainer(context)
            toggle.setData(labels)
            return toggle
        }
    }
}

class LabelLayoutContainer : FrameLayout {
    // <editor-fold defaultstate="collapsed" desc="构造函数">
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    // </editor-fold>

    private fun initView(context: Context?) {
        LayoutInflater.from(context).inflate(R.layout.label_layout_container, this, true)
    }

    fun setData(labels: Array<String>) {
        toggle.setMin(150)
        val box = LabelFlowLayout(context)
        val p = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val value = 6.dp
        p.setMargins(value, value, value, value)
        for (text in labels) {
            val item = LayoutInflater.from(context).inflate(R.layout.label_tv, null)
            val tv = item.findViewById<TextView>(R.id.text)
            tv.text = text
            box.addView(item, p)
        }
        toggle.addContentView(box, ToggleLayout.Status.CLOSE)
    }
}