package com.engineer.imitate.fragments


import android.app.WallpaperManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.engineer.imitate.R
import kotlinx.android.synthetic.main.fragment_fresco.*


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

        val player = "http:\\/\\/img.dongqiudi.com\\/data\\/personpic\\/15482.png"

        simpleDraweeView.setImageURI(url)
        Glide.with(this).load(url).into(image)


        val wallpaperManager = WallpaperManager.getInstance(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val isSetWallpaperAllowed = wallpaperManager.isSetWallpaperAllowed
            val isWallpaperSupported = wallpaperManager.isWallpaperSupported

            Log.e("wall paper", "isSetWallpaperAllowed==$isSetWallpaperAllowed")
            Log.e("wall paper", "isWallpaperSupported ==$isWallpaperSupported")
        }

    }


}
