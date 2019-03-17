package com.engineer.imitate.fragments


import android.animation.ValueAnimator
import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_fresco.*
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/fresco")
class FrescoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fresco, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = "http://h.hiphotos.baidu.com/image/pic/item/960a304e251f95ca060674a0c7177f3e67095231.jpg"

        simpleDraweeView.setImageURI(url)


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


}
