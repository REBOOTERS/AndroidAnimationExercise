package home.smart.fly.animations.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import home.smart.fly.animations.R
import home.smart.fly.animations.utils.Glide4Engine
import home.smart.fly.animations.utils.gif.GifFactory
import kotlinx.android.synthetic.main.activity_reverse_gif.*

class ReverseGifActivity : AppCompatActivity() {

    // <editor-fold defaultstate="collapsed" desc="onCreate">

    private lateinit var mContext: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        setContentView(R.layout.activity_reverse_gif)
        start.setOnClickListener {
            selectGif()
        }
    }
    // </editor-fold>

    @SuppressLint("CheckResult")
    private fun selectGif() {

        getResourceByLoad(R.drawable.haha)

        val permissions = RxPermissions(this)
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe {
                    Matisse.from(this)
                            .choose(MimeType.of(MimeType.GIF))
                            .countable(false)
                            .capture(false)
                            .captureStrategy(
                                    CaptureStrategy(true, mContext.packageName + ".fileprovider"))
                            .maxSelectable(1)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                            .thumbnailScale(0.85f)
                            .imageEngine(Glide4Engine())
                            .forResult(GIF_REQUEST_CODE)
                }
    }


    @SuppressLint("CheckResult")
    private fun getResourceByLoad(source: Int?) {
        GifFactory.getReverseRes(mContext, source)
                .subscribe({
                    Glide.with(mContext).load(it).into(reversed)
                    // 原图和反转图同时加载，看看效果
                    Glide.with(mContext).load(source).into(original)
                }, {
                    it.printStackTrace()
                })
    }


    // <editor-fold defaultstate="collapsed" desc="onActivityResult">
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GIF_REQUEST_CODE) {
            val result = Matisse.obtainPathResult(data)[0]
//            getResourceByLoad(result)
        }
    }
    // </editor-fold>

    companion object {
        val GIF_REQUEST_CODE = 100
    }
}
