package com.engineer.imitate.ui.activity

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.engineer.imitate.R
import com.engineer.imitate.databinding.ActivityScreenRecorderBinding
import com.engineer.imitate.services.MediaRecordService
import com.engineer.imitate.ui.widget.headsup.Pudding
import com.engineer.imitate.util.PathUtils
import com.engineer.imitate.util.ScreenRecordHelper
import com.engineer.imitate.util.SpUtil
import java.io.File

class ScreenRecorderActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityScreenRecorderBinding
    private var screenRecordHelper: ScreenRecordHelper? = null
    private val music: AssetFileDescriptor by lazy { assets.openFd("test.aac") }
    private val fit_key = "fit"

    private var fit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fit = SpUtil(this).getBool(fit_key)
        println("value ==$fit")
//        window.decorView.fitsSystemWindows = fit
        viewBinding = ActivityScreenRecorderBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.start.setOnClickListener {
            record()
        }

        viewBinding.stop.setOnClickListener {
            screenRecordHelper?.apply {
                if (isRecording) {
                    if (mediaPlayer != null) {
                        // 如果选择带参数的 stop 方法，则录制音频无效
                        stopRecord(mediaPlayer?.duration?.toLong() ?: 0, 15 * 1000, music)
                    } else {
                        stopRecord()
                    }
                }
            }
        }

        viewBinding.revert.setOnClickListener {
            fit = !fit
//            window.decorView.fitsSystemWindows = !fit
            SpUtil(this).saveBool(fit_key, fit)
            if (fit) {
                viewBinding.revert.text = "true"
            } else {
                viewBinding.revert.text = "false"
            }
            recreate()
        }

        viewBinding.headsup.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.item_heads_up, null)
            Log.e("zyq", "on==view==h==${view.measuredHeight}")
            Pudding.create(this, view) {}
                .show()
        }

        viewBinding.headsupImg.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.item_heads_up, null)
            val target = view.findViewById<ImageView>(R.id.headsup_image)
            Glide.with(this)
                .load("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg")
                .into(target)
            Log.e("zyq", "on==view==h==${view.measuredHeight}")
            target.setOnClickListener { Toast.makeText(this, "1", Toast.LENGTH_SHORT).show() }
            Pudding.create(this, view) {}
                .show()
        }

    }

    private fun record() {
        if (screenRecordHelper == null) {
            screenRecordHelper = ScreenRecordHelper(
                this,
                object : ScreenRecordHelper.OnVideoRecordListener {
                    override fun onBeforeRecord() {
                    }

                    override fun onStartRecord() {
                        play()
                    }

                    override fun onCancelRecord() {
                        releasePlayer()
                    }

                    override fun onEndRecord() {
                        releasePlayer()
                    }

                },
                PathUtils.getExternalStoragePath() + File.separator + getString(R.string.app_name)
            )
        }
        screenRecordHelper?.apply {
            if (!isRecording) {
                // 如果你想录制音频（一定会有环境音量），你可以打开下面这个限制,并且使用不带参数的 stopRecord()
//                        recordAudio = true
                startRecord()
            }
        }
    }

    private fun play() {
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer?.apply {
                this.reset()
                this.setDataSource(music.fileDescriptor, music.startOffset, music.length)
                this.isLooping = true
                this.prepare()
                this.start()
            }
        } catch (e: Exception) {
            Log.d("screen", "播放音乐失败")
        } finally {

        }
    }

    // 音频播放
    private var mediaPlayer: MediaPlayer? = null

    private fun releasePlayer() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    @Deprecated("don't use", replaceWith = ReplaceWith("ActivityResultLauncher"))
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                val service = Intent(this@ScreenRecorderActivity, MediaRecordService::class.java)
                startForegroundService(service)
            } else {
                screenRecordHelper?.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onDestroy() {
        screenRecordHelper?.clearAll()
        music.close()
        super.onDestroy()
    }

}
