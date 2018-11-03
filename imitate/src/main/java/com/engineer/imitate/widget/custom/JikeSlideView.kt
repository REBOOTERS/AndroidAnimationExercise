package com.engineer.imitate.widget.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.engineer.imitate.util.dp2px

/**
 *
 * @author: zhuyongging
 * @since: 2018-11-03
 * @desc
 */

class JikeSlideView : View {

    private val TAG = "JikeSlideView"

    //<editor-fold desc="init">
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }
    //</editor-fold>

    private lateinit var wavePath: Path
    private lateinit var arrowPath: Path

    private lateinit var wavePaint: Paint
    private lateinit var arrowPaint: Paint

    private var deltaX = 0.0f

    private var waveHeight = 0.0f
    private var waveWidth = 40

    private var screenWidth = 0
    private var screenHeight = 0

    private var canvasPositionY = 0.0f


    private fun init(context: Context) {
        waveHeight = context.dp2px(260.0f)

        wavePath = Path()
        arrowPath = Path()

        wavePaint = Paint()
        wavePaint.isAntiAlias = true
        wavePaint.style = Paint.Style.FILL_AND_STROKE
        wavePaint.color = Color.parseColor("#B3000000")
        wavePaint.strokeWidth = 1.0f

        arrowPaint = Paint()
        arrowPaint.isAntiAlias = true
        arrowPaint.style = Paint.Style.FILL_AND_STROKE
        arrowPaint.color = Color.WHITE
        arrowPaint.strokeWidth = 6.0f

        alpha = 1.0f

        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels
    }

    fun update(deltaX: Float, deltaY: Float) {
        Log.e(TAG, "deltaX==$deltaX")
        Log.e(TAG, "deltaY==$deltaY")
        Log.e(TAG, "waveHeight==$waveHeight")

        this.deltaX = deltaX
        this.canvasPositionY = deltaY
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(0.0f, canvasPositionY  - waveHeight/2)

        wavePath.reset()
        wavePath.moveTo(0.0f, 0.0f)
        wavePath.quadTo(0.0f, waveHeight / 4, deltaX / 3, waveHeight * 3 / 8)
        wavePath.quadTo(deltaX * 5 / 8, waveHeight / 2, deltaX / 3, waveHeight * 5 / 8)
        wavePath.quadTo(0.0f, waveHeight * 6 / 8, 0.0f, waveHeight)
        canvas.drawPath(wavePath, wavePaint)

        arrowPath.reset()
        arrowPath.moveTo(deltaX / 6 + (context.dp2px(4.0f) * (deltaX / (screenWidth / 6))), waveHeight * 15 / 32)
        arrowPath.lineTo(deltaX / 6, waveHeight * 16.1f / 32)
        arrowPath.moveTo(deltaX / 6, waveHeight * 15.9f / 32)
        arrowPath.lineTo(deltaX / 6 + (context.dp2px(4.0f) * (deltaX / (screenWidth / 6))), waveHeight * 17 / 32)
        canvas.drawPath(arrowPath, arrowPaint)

        alpha = deltaX / (screenWidth / 6)
    }
}