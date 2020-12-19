package com.engineer.imitate.ui.activity.surface.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

abstract class SimpleSurfaceView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null
) :
    SurfaceView(context, attributes), SurfaceHolder.Callback,
    Runnable {


    var surfaceHolder: SurfaceHolder = holder
    private var canvas: Canvas? = null
    private var t: Thread? = null
    private var isRunning = false

    init {

        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT)
        isFocusable = true
        isFocusableInTouchMode = true
        keepScreenOn = true


    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        surfaceHolder.addCallback(this)
        setZOrderOnTop(true)
        isRunning = true
        t = Thread()
        t?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        isRunning = false
        surfaceHolder.removeCallback(this)
    }

    override fun run() {
        while (isRunning) {
            doDrawStuff()
        }
    }

    private fun doDrawStuff() {
        try {
            canvas = surfaceHolder.lockCanvas()
            canvas?.run {
                realDraw(this)
            }
        } catch (e: Exception) {
        } finally {
            canvas?.let {
                surfaceHolder.unlockCanvasAndPost(it)
            }
        }
    }

    abstract fun realDraw(canvas: Canvas)
}