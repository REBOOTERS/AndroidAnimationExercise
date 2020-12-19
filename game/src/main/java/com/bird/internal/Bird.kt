package com.bird.internal

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF

class Bird(
    gameWith: Int, gameHeight: Int,
    /**
     * 鸟的bitmap
     */
    private val bitmap: Bitmap
) {
    /**
     * 鸟的横坐标
     */
    val x: Int

    /**
     * 鸟的纵坐标
     */
    var y: Int

    /**
     * 鸟的宽度
     */
    val width: Int

    /**
     * 鸟的高度
     */
    val height: Int

    /**
     * 鸟绘制的范围
     */
    private val rect = RectF()
    private val mGameHeight: Int

    fun resetHeight() {
        y = (mGameHeight * RADIO_POS_HEIGHT).toInt()
    }

    fun draw(canvas: Canvas) {
        rect[x.toFloat(), y.toFloat(), (x + width).toFloat()] = (y + height).toFloat()
        canvas.drawBitmap(bitmap, null, rect, null)
    }

    companion object {
        /**
         * 鸟在屏幕高度的2/3位置
         */
        private const val RADIO_POS_HEIGHT = 1 / 2f

        /**
         * 鸟的宽度 30dp
         */
        private const val BIRD_SIZE = 30
    }

    init {
        //鸟的位置
        x = gameWith / 2 - bitmap.width / 2
        y = (gameHeight * RADIO_POS_HEIGHT).toInt()
        mGameHeight = gameHeight

        // 计算鸟的宽度和高度
        width = BIRD_SIZE.dp
        height = (width * 1.0f / bitmap.width * bitmap.height).toInt()
    }
}