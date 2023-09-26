package com.bird.internal

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader.TileMode

class Floor(private val mGameWidth: Int, private val mGameHeight: Int, floorBg: Bitmap?) {
    /**
     * x坐标
     */
    var x = 0

    /**
     * y坐标
     */
    val y: Int

    /**
     * 填充物
     */
    private val mFloorShader: BitmapShader

    fun draw(mCanvas: Canvas, mPaint: Paint) {
        if (-x > mGameWidth) {
            x %= mGameWidth
        }
        mCanvas.saveLayer(0f, 0f, mGameWidth.toFloat(), mGameHeight.toFloat(), null)
        //移动到指定的位置
        mCanvas.translate(x.toFloat(), y.toFloat())
        mPaint.shader = mFloorShader
        mCanvas.drawRect(
            x.toFloat(),
            0f,
            (-x + mGameWidth).toFloat(),
            (mGameHeight - y).toFloat(),
            mPaint
        )
        mCanvas.restore()
        mPaint.shader = null
    }

    companion object {
        /*
     * 地板位置游戏面板高度的4/5到底部
     */
        private const val FLOOR_Y_POS_RADIO = 4 / 5f
    }

    init {
        y = (mGameHeight * FLOOR_Y_POS_RADIO).toInt()
        mFloorShader = BitmapShader(
            floorBg!!, TileMode.REPEAT,
            TileMode.CLAMP
        )
    }
}