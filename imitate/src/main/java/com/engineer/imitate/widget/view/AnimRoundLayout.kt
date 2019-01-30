package com.engineer.imitate.widget.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import org.xml.sax.Attributes

/**
 *
 * @author: zhuyongging
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