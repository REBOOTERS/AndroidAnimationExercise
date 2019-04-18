package com.engineer.imitate.ui.fragments


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.engineer.imitate.R
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_fresco.*


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/fresco")
class FrescoFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fresco, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = "http://h.hiphotos.baidu.com/image/pic/item/960a304e251f95ca060674a0c7177f3e67095231.jpg"

        simpleDraweeView.setImageURI(url)

        bitmapMagic()


        val drawable = imageView.background as ClipDrawable
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.totoro)
//        drawable.drawable = BitmapDrawable(resources,bitmap)

        Glide.with(this).load(url).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    drawable.drawable = resource
                }
            }

        })

        drawable.level = 0


        val values = ValueAnimator.ofInt(0, 1000)
        values.duration = 3000
        values.addUpdateListener { animation ->
            val value = (animation!!.animatedValue) as Int
            Log.e("anim", "value===$value")
            drawable.level = 10 * (value)
        }
        values.start()


        imageView.setOnClickListener {
            if (drawable.level == 0) {
                values.start()
            } else {
                values.reverse()
            }
        }


        val wallpaperManager = WallpaperManager.getInstance(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val isSetWallpaperAllowed = wallpaperManager.isSetWallpaperAllowed
            val isWallpaperSupported = wallpaperManager.isWallpaperSupported

            Log.e("wall paper", "isSetWallpaperAllowed==$isSetWallpaperAllowed")
            Log.e("wall paper", "isWallpaperSupported ==$isWallpaperSupported")
        }

    }

    @SuppressLint("CheckResult")
    private fun bitmapMagic() {
        shimmer_layout.startShimmerAnimation()

        Observable.create(object : ObservableOnSubscribe<Bitmap> {
            override fun subscribe(emitter: ObservableEmitter<Bitmap>) {
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.star)
                val width = bitmap.width
                val height = bitmap.height
                val colorArray = Array(width) { IntArray(height) }
                for (i in 0 until width) {
                    for (j in 0 until height) {
                        colorArray[i][j] = bitmap.getPixel(i, j)
                    }
                }

                Log.e("bitmapMagic", "colorArray==$colorArray")
                emitter.onNext(bitmap)
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bitmap ->
                    setupBitmap(bitmap)
                }, { t -> t.printStackTrace() })

    }

    private fun setupBitmap(bitmap: Bitmap) {


        val drawBitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
        drawBitmap.setPixel(0, 0, Color.BLACK)
        drawBitmap.setPixel(1, 0, Color.RED)
        drawBitmap.setPixel(0, 1, Color.WHITE)
        drawBitmap.setPixel(1, 1, Color.BLUE)

        draw_bitmap.setImageBitmap(drawBitmap)
        shimmer_layout.stopShimmerAnimation()
        mask.visibility = View.GONE
    }


}
