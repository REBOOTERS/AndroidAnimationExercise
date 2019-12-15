package home.smart.fly.animations.utils

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * @author rookie
 * @since 12-14-2019
 */
object ComposeView {

    @JvmStatic
    fun composeBitmap(background: Bitmap, foreground: Bitmap, left: Float, top: Float): Bitmap {
        val result =
            Bitmap.createBitmap(background.width, background.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawBitmap(foreground, left, top, null)
        canvas.save()
        canvas.restore()
        background.recycle()
        foreground.recycle()
        return result
    }
}