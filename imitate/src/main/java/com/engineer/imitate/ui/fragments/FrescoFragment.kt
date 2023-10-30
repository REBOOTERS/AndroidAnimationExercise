package com.engineer.imitate.ui.fragments


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.didichuxing.doraemonkit.util.ScreenUtils
import com.engineer.imitate.R
import com.engineer.imitate.databinding.FragmentFrescoBinding
import com.engineer.imitate.util.dp
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.references.CloseableReference
import com.facebook.common.util.UriUtil
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/fresco")
class FrescoFragment : Fragment() {
    private lateinit var viewBinding: FragmentFrescoBinding
    val c: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentFrescoBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    @SuppressLint("LogNotTimber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                super.onFinalImageSet(id, imageInfo, animatable)
                imageInfo?.let {
                    val w1 = it.width
                    val h1 = it.height
                    val width = ScreenUtils.getScreenWidth() - 16.dp
                    val height = ScreenUtils.getScreenHeight()
                    Log.d("Fresco", "onFinalImageSet() called w1=$w1,h1=$h1")
                    Log.d("Fresco", "onFinalImageSet() called w=$width,h=$height")
                    Log.d("Fresco", "onFinalImageSet() called ${it.qualityInfo.isOfFullQuality}")
                    Log.d("Fresco", "onFinalImageSet() called ${it.qualityInfo.isOfGoodEnoughQuality}")
                    Log.d("Fresco", "onFinalImageSet() called ${it.extras}")
                    viewBinding.s0.layoutParams.width = width
//                    viewBinding.s0.layoutParams.height = h1
                    viewBinding.s0.aspectRatio = 1f * width / height
                }
//                viewBinding.s0.layoutParams.width = imageInfo?.width ?: 0

            }
        }

        val controller: DraweeController =
            Fresco.newDraweeControllerBuilder().setUri(UriUtil.getUriForResourceId(R.drawable.anim))
                .setControllerListener(listener).setAutoPlayAnimations(true).build()
        viewBinding.s0.controller = controller

        viewBinding.s1.setActualImageResource(R.drawable.totoro)


        //获取GenericDraweeHierarchy对象
        //获取GenericDraweeHierarchy对象
        val hierarchy: GenericDraweeHierarchy = GenericDraweeHierarchyBuilder(resources).build()
        val roundingParams = RoundingParams()
        roundingParams.roundAsCircle = true
        roundingParams.borderWidth = 10f
        roundingParams.borderColor = Color.BLUE
        hierarchy.roundingParams = roundingParams
        viewBinding.s2.hierarchy = hierarchy
        viewBinding.s2.setActualImageResource(R.drawable.totoro)

        val url = "http://h.hiphotos.baidu.com/image/pic/item/960a304e251f95ca060674a0c7177f3e67095231.jpg"


        val t = System.currentTimeMillis()
        viewBinding.simpleDraweeView.setImageURI(url)
        Log.e("zyq", "use time=" + (System.currentTimeMillis() - t))
        viewBinding.simpleDraweeView
        bitmapMagic()


        val drawable = viewBinding.imageView.background as ClipDrawable
//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.totoro)
//        drawable.drawable = BitmapDrawable(resources,bitmap)

        Glide.with(this).load(url).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    drawable.drawable = resource
                }
            }
        })

        val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url)).build()
        Fresco.getImagePipeline().fetchDecodedImage(request, url)

            .subscribe(object : BaseBitmapDataSubscriber() {
                override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>) {
                    Log.e("zyq", "fail $dataSource")
                }

                override fun onNewResultImpl(bitmap: Bitmap?) {
                    if (bitmap != null) {
                        Log.e(
                            "zyq", "thread ==${Thread.currentThread().name}"
                        )
                        viewBinding.imageView1.post {
                            viewBinding.imageView1.setImageBitmap(bitmap)
                        }
                    }
                }

            }, CallerThreadExecutor.getInstance())



        drawable.level = 0
        val values = ValueAnimator.ofInt(0, 1000)
        values.duration = 3000
        values.addUpdateListener { animation ->
            val value = (animation!!.animatedValue) as Int
//            Log.e("anim", "value===$value")
            drawable.level = 10 * (value)
        }
        values.start()


        viewBinding.imageView.setOnClickListener {
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

    private fun bitmapMagic() {
        viewBinding.shimmerLayout.startShimmerAnimation()

        c.add(Observable.create(ObservableOnSubscribe<Bitmap> { emitter ->
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.star)
            val width = bitmap.width
            val height = bitmap.height
            val colorArray = Array(width) { IntArray(height) }
            for (i in 0 until width) {
                for (j in 0 until height) {
//                    Log.e("zyq", "i==$i j==$j")
//                    Log.e("zyq", "live=${emitter.isDisposed}")
                    if (emitter.isDisposed) {
                        break
                    }
                    colorArray[i][j] = bitmap.getPixel(i, j)
                }
            }
            Log.e("bitmapMagic", "colorArray==$colorArray")
            emitter.onNext(bitmap)
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ bitmap ->
            setupBitmap(bitmap)
        }, { t -> t.printStackTrace() })
        )

    }

    private fun setupBitmap(bitmap: Bitmap) {


        val drawBitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
        drawBitmap.setPixel(0, 0, Color.BLACK)
        drawBitmap.setPixel(1, 0, Color.RED)
        drawBitmap.setPixel(0, 1, Color.WHITE)
        drawBitmap.setPixel(1, 1, Color.BLUE)

        if (viewBinding.drawBitmap == null) {
            return
        }
        viewBinding.drawBitmap.setImageBitmap(drawBitmap)
        viewBinding.shimmerLayout.stopShimmerAnimation()
        viewBinding.mask.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("zyq", "onDestroyView")
        c.clear()
    }

    override fun onStop() {
        super.onStop()
        Log.e("zyq", "onStop")
    }


}
