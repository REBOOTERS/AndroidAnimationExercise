package com.engineer.imitate.ui.widget.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.engineer.imitate.util.dp
import com.engineer.imitate.util.dp2px

/**
 *
 * @author: rookie
 * @since: 2018-11-03
 * @desc
 */

class SlideArrowView : View {

    private val TAG = "SlideArrowView"

    //<editor-fold desc="init">
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }
    //</editor-fold>

    private lateinit var arrowPath: Path

    private lateinit var arrowPaint: Paint

    private var deltaX = 0.0f

    private var waveHeight = 0.0f

    private var screenWidth = 0
    private var screenHeight = 0


    private fun init(context: Context) {
        waveHeight = context.dp2px(260.0f)

        arrowPath = Path()


        arrowPaint = Paint()
        arrowPaint.isAntiAlias = true
        arrowPaint.strokeCap = Paint.Cap.ROUND
        arrowPaint.style = Paint.Style.STROKE
        arrowPaint.color = Color.parseColor("#646464")
        arrowPaint.strokeWidth = 10.0f

        alpha = 1.0f

        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels
    }

    fun update(deltaX: Float) {

        this.deltaX = deltaX * 200
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(0.0f, screenHeight / 2 - waveHeight / 2)

        val delta = 12.dp * this.deltaX / (screenWidth / 6)
        arrowPath.reset()
        arrowPath.moveTo(deltaX / 6, waveHeight * 14 / 32)
        if (delta > 0) {
            arrowPath.lineTo(deltaX / 6 + delta, waveHeight * 16f / 32)
        }
        arrowPath.lineTo(deltaX / 6, waveHeight * 18 / 32)
        canvas.drawPath(arrowPath, arrowPaint)
    }
}