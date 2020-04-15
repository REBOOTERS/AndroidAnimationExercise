package com.engineer.imitate.ui.widget.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 *
 * @authro: Rookie
 * @since: 2019-01-28
 */
class AnimRoundLayout : FrameLayout {

    constructor(context: Context):super(context)
    constructor(context: Context,attrs: AttributeSet):super(context,attrs)
    constructor(context: Context,attrs: AttributeSet,style:Int):super(context,attrs,style)

    public fun update(offset: Float) {
        if (offset < 2) {
            return
        }
    }
}