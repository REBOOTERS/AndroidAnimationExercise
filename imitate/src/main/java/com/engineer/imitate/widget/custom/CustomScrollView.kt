package com.engineer.imitate.widget.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Scroller

/**
 *
 * @author: zhuyongging
 * @date: 2018-10-30
 * @desc
 */
class CustomScrollView: View {

    private lateinit var scroller: Scroller

    //<editor-fold desc="init">
    constructor(context:Context) :super(context) {init(context)}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }
    //</editor-fold>

    fun init(context: Context){
        scroller=Scroller(context)
    }
}