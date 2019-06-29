package com.engineer.imitate.ui.activity

import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.engineer.imitate.R
import com.engineer.imitate.util.PathUtils
import com.engineer.imitate.util.ScreenRecordHelper
import kotlinx.android.synthetic.main.activity_screen_recorder.*
import kotlinx.android.synthetic.main.content_screen_recoder.*
import java.io.File

class ScreenRecorderActivity : AppCompatActivity() {

    private var screenRecordHelper: ScreenRecordHelper? = null
    private val music: AssetFileDescriptor by lazy { assets.openFd("test.aac") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_recorder)
        setSupportActionBar(toolbar)

        start.setOnClickListener {
            record()
        }

        stop.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                screenRecordHelper?.apply {
                    if (isRecording) {
                        if (mediaPlayer != null) {
                            // 如果选择带参数的 stop 方法，则录制音频无效
                            stopRecord(mediaPlayer!!.duration.toLong(), 15 * 1000, music)
                        } else {
                            stopRecord()
                        }
                    }
                }
            }
        }


    }

    private fun record() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (screenRecordHelper == null) {
                screenRecordHelper = ScreenRecordHelper(this, object : ScreenRecordHelper.OnVideoRecordListener {
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

                }, PathUtils.getExternalStoragePath() + File.separator + getString(R.string.app_name))
            }
            screenRecordHelper?.apply {
                if (!isRecording) {
                    // 如果你想录制音频（一定会有环境音量），你可以打开下面这个限制,并且使用不带参数的 stopRecord()
//                        recordAudio = true
                    startRecord()
                }
            }
        } else {
            Toast.makeText(this@ScreenRecorderActivity, "sorry,your phone does not support recording screen", Toast.LENGTH_LONG).show()
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
            Log.d("nanchen2251", "播放音乐失败")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && data != null) {
            screenRecordHelper?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            screenRecordHelper?.clearAll()
        }
        music.close()
        super.onDestroy()
    }

}
