package com.engineer.imitate.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.bumptech.glide.Glide
import com.engineer.gif.revert.GifFactory
import com.engineer.imitate.R
import com.engineer.imitate.databinding.ActivityReverseGifBinding
import com.engineer.imitate.util.Glide4Engine
import com.engineer.imitate.util.toastShort
import com.permissionx.guolindev.PermissionX
import com.skydoves.transformationlayout.onTransformationEndContainer
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy

class ReverseGifActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityReverseGifBinding

    // <editor-fold defaultstate="collapsed" desc="onCreate">

    private lateinit var mContext: Context
    private var originalUrl: Uri? = null
    private var revertedlUrl: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra("TransformationParams"))
        super.onCreate(savedInstanceState)
        mContext = this
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        viewBinding = ActivityReverseGifBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.start.setOnClickListener {
            selectGif()
        }

        viewBinding.share.setOnClickListener {
            if (originalUrl != null && revertedlUrl != null) {
                val shareIntent = ShareCompat.IntentBuilder.from(this).addStream(originalUrl ?: Uri.EMPTY)
                    .addStream(revertedlUrl ?: Uri.EMPTY).setText("反转 gif").setType("text/richtext")
                    .createChooserIntent().addFlags(
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    )
                startActivity(shareIntent)
            } else {
                mContext.toastShort("请选择图片先，😜")
            }
        }

        Glide.with(this).load(R.drawable.haha).into(viewBinding.original)
        Glide.with(this).load(R.drawable.haha_revert).into(viewBinding.reversed)

    }
    // </editor-fold>

    @SuppressLint("CheckResult")
    private fun selectGif() {
        PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE).request { it, _, _ ->
                if (it) {
                    Matisse.from(this).choose(MimeType.of(MimeType.GIF)).showSingleMediaType(true).countable(false)
                        .capture(false).captureStrategy(
                            CaptureStrategy(true, mContext.packageName + ".fileprovider")
                        ).maxSelectable(1).restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                        .thumbnailScale(0.85f).imageEngine(Glide4Engine()).forResult(GIF_REQUEST_CODE)
                }
            }
    }


    @SuppressLint("CheckResult")
    private fun doRevert(source: String?) {

        viewBinding.loading.visibility = View.VISIBLE
        viewBinding.result.text = "转换中 ......."
        viewBinding.timer.base = SystemClock.elapsedRealtime()
        viewBinding.timer.start()

        GifFactory.getReverseRes(mContext, source).subscribe({
                viewBinding.loading.visibility = View.GONE

                originalUrl = Uri.parse(source)
                revertedlUrl = Uri.parse(it)
                viewBinding.result.text = "图片保存在 :$it"
                viewBinding.timer.stop()

                Glide.with(mContext).load(it).into(viewBinding.reversed)
                // 原图和反转图同时加载，看看效果
                Glide.with(mContext).load(source).into(viewBinding.original)
            }, {
                it.printStackTrace()
                viewBinding.loading.visibility = View.GONE
                viewBinding.timer.stop()
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
            })
    }


    // <editor-fold defaultstate="collapsed" desc="onActivityResult">
    @Deprecated("don't use", replaceWith = ReplaceWith("ActivityResultLauncher"))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == GIF_REQUEST_CODE) {
            val result = Matisse.obtainPathResult(data)[0]
            if (result.endsWith(".gif")) {
                doRevert(result)
            } else {
                mContext.toastShort("not gif")
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="revert drawable">
    @SuppressLint("CheckResult")
    private fun doRevert(source: Int?) {
        viewBinding.loading.visibility = View.VISIBLE
        GifFactory.getReverseRes(mContext, source).subscribe({
                viewBinding.loading.visibility = View.GONE
                Glide.with(mContext).load(it).into(viewBinding.reversed)
                // 原图和反转图同时加载，看看效果
                Glide.with(mContext).load(source).into(viewBinding.original)
            }, {
                it.printStackTrace()
            })
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="companion object">
    companion object {
        val GIF_REQUEST_CODE = 100
    }
    // </editor-fold>
}
