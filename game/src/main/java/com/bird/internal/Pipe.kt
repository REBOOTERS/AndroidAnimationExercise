package com.bird.internal

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import java.util.*

class Pipe(
    private val gameWidth: Int, private val gameHeight: Int, top: Bitmap,
    bottom: Bitmap
) {
    /**
     * 管道的横坐标
     */
    var x: Int

    /**
     * 上管道的高度
     */
    var height = 0

    /**
     * 上下管道间的距离
     */
    val margin: Int

    /**
     * 上管道图片
     */
    private val mTop: Bitmap

    /**
     * 下管道图片
     */
    private val mBottom: Bitmap

    /**
     * 随机生成一个高度
     */
    private fun randomHeight(gameHeight: Int) {
        height = random.nextInt((gameHeight * (RADIO_MAX_HEIGHT - RADIO_MIN_HEIGHT)).toInt())
        height = (height + gameHeight * RADIO_MIN_HEIGHT).toInt()
    }

    fun draw(mCanvas: Canvas, rect: RectF) {
        mCanvas.saveLayer(0f, 0f, gameWidth.toFloat(), gameHeight.toFloat(), null)
        // rect为整个管道，假设完整管道为100，需要绘制20，则向上偏移80
        mCanvas.translate(x.toFloat(), -(rect.bottom - height))
        mCanvas.drawBitmap(mTop, null, rect, null)
        // 下管道，便宜量为，上管道高度+margin
        mCanvas.translate(0f, rect.bottom - height + height + margin)
        mCanvas.drawBitmap(mBottom, null, rect, null)
        mCanvas.restore()
    }

    /**
     * 判断和鸟是否触碰
     *
     * @param mBird
     * @return
     */
    fun touchBird(mBird: Bird): Boolean {
        /**
         * 如果bird已经触碰到管道
         */
        return (mBird.x + mBird.width > x
                && (mBird.y < height || mBird.y + mBird.height > height
                + margin))
    }

    companion object {
        /**
         * 上下管道间的距离
         */
        private const val RADIO_BETWEEN_UP_DOWN = 1.5f / 5f

        /**
         * 上管道的最大高度
         */
        private const val RADIO_MAX_HEIGHT = 2 / 5f

        /**
         * 上管道的最小高度
         */
        private const val RADIO_MIN_HEIGHT = 1 / 5f

        private val random = Random()
    }

    init {
        margin = (gameHeight * RADIO_BETWEEN_UP_DOWN).toInt()
        // 默认从最左边出现
        x = gameWidth
        mTop = top
        mBottom = bottom
        randomHeight(gameHeight)
    }
}