package home.smart.fly.animations.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.PixelCopy
import android.view.SurfaceView
import android.view.View

/**
 * @author rookie
 * @since 12-14-2019
 */
class GenBitmapDelegate(private val activity: Activity) {

    fun composeBitmap(background: Bitmap, foreground: Bitmap, left: Float, top: Float): Bitmap {
        val result =
                Bitmap.createBitmap(background.width, background.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawBitmap(foreground, left, top, null)
        canvas.save()
        canvas.restore()
        return result
    }


    @SuppressLint("NewApi")
    fun getBitmapFromSurfaceView(surfaceView: SurfaceView, callback: (bitmap: Bitmap) -> Unit) {

        val result = Bitmap.createBitmap(surfaceView.width,
                surfaceView.height, Bitmap.Config.ARGB_8888)

        if (SysUtil.Android8OrLater()) {
            PixelCopy.request(surfaceView, result, { copyResult: Int ->
                callback(result)
            }, Handler(Looper.getMainLooper()))
        } else {
            callback(result)

        }


    }

    fun getBitmapByDrawCacheExclueSystemBar(viewRoot: View): Bitmap {
        viewRoot.isDrawingCacheEnabled = true
        val temp = viewRoot.drawingCache
        val screenInfo: ScreenParam = getScreenInfo(activity)
        val statusBarHeight: Int = getStatusBarHeight(activity)
        val bitmap = Bitmap.createBitmap(temp, 0, statusBarHeight, screenInfo.width, screenInfo.height - statusBarHeight)
        viewRoot.isDrawingCacheEnabled = false
        return bitmap
    }

    fun getBitmapByDrawCache(viewRoot: View): Bitmap {
        viewRoot.isDrawingCacheEnabled = true
        val bitmap = viewRoot.drawingCache
        return bitmap
    }


    private fun getStatusBarHeight(activity: Activity): Int {
        val rect = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rect)
        return rect.top
    }

    private fun getScreenInfo(activity: Activity): ScreenParam {
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        return ScreenParam(dm.widthPixels, dm.heightPixels)
    }

    private class ScreenParam internal constructor(var width: Int, var height: Int)


}