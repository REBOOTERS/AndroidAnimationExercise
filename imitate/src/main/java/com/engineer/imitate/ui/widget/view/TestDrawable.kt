package com.engineer.imitate.ui.widget.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 12-26-2019
 */
class TestDrawable:ShapeDrawable() {

    private lateinit var mPaint: Paint
    private var mStrokeWidth = 5f
    private var mRadius = 5f
    private var mColor = Color.BLUE
    private val maxCount = 3
    private var count = 0
    private lateinit var rect: RectF
    private var OneDp = 0f
    private lateinit var mContext: Context

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
    }
}