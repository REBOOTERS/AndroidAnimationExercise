package com.engineer.imitate.ui.activity

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.TraceCompat
import com.engineer.imitate.R
import com.engineer.imitate.util.CropCircleHelper
import com.engineer.imitate.util.dp2px
import kotlinx.android.synthetic.main.activity_self_draw_view.*


class SelfDrawViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_draw_view)
        val _100dp = dp2px(100f)
        val b = BitmapFactory.decodeResource(resources, R.drawable.girl)

        TraceCompat.beginSection("cropCircle")
        val b2 = CropCircleHelper.cropCircle(b, _100dp.toInt())
        TraceCompat.endSection()
        scaled_view.setImageBitmap(b2)
        drawBitmap("mike", b2) {
            runOnUiThread {
                if (isFinishing.not()) {
                    self_draw.setImageBitmap(it)
                }
            }
        }
    }

    private fun drawBitmap(name: String, b: Bitmap, callback: (Bitmap) -> Unit) {
        Thread {
            val w = resources.displayMetrics.widthPixels
            val h = resources.displayMetrics.heightPixels
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawColor(Color.parseColor("#ff00719b"))
            val radius = dp2px(100f).toInt()
            val rect = Rect(w / 2 - radius, h / 2 - radius, w / 2 + radius, h / 2 + radius)
            canvas.drawBitmap(b, null, rect, null)

            val paint = TextPaint()
            paint.color = Color.WHITE
            paint.textAlign = Paint.Align.CENTER
            paint.textSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                16f, resources.displayMetrics
            )
            canvas.drawText(name, w / 2f, h / 2f + dp2px(120f), paint)
            Thread.sleep(2000)
            callback(bitmap)
        }.start()
    }

    class SimpleCircle : View {
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
        var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val size = context.dp2px(100f).toInt()
        private fun init(c: Context) {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.girl)
            paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            canvas?.drawCircle(size.toFloat(), size.toFloat(), size.toFloat(), paint)
        }
    }
}