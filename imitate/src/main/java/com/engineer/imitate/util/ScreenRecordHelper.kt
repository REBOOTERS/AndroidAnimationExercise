package com.engineer.imitate.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.*
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.engineer.imitate.R
import java.io.File
import java.nio.ByteBuffer


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ScreenRecordHelper @JvmOverloads constructor(
        private var activity: Activity,
        private val listener: OnVideoRecordListener?,
        private var savePath: String = Environment.getExternalStorageDirectory().absolutePath + File.separator
                + "DCIM" + File.separator + "Camera",
        private val saveName: String = "nanchen_${System.currentTimeMillis()}"
) {

    private val mediaProjectionManager by lazy {
        activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as? MediaProjectionManager
    }
    private var mediaRecorder: MediaRecorder? = null
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private val displayMetrics by lazy { DisplayMetrics() }
    private var saveFile: File? = null
    var isRecording = false
    var recordAudio = false

    init {
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    }

    fun startRecord() {
        if (mediaProjectionManager == null) {
            Log.d(TAG, "mediaProjectionManager == null，当前手机暂不支持录屏")
            showToast(R.string.phone_not_support_screen_record)
            return
        }

        val permissionsGranted: Boolean = activity.isAllGranted(Permission.WRITE_EXTERNAL_STORAGE,
                Permission.CAMERA)
        activity.askForPermissions(Permission.WRITE_EXTERNAL_STORAGE,
                Permission.CAMERA) {
            if (permissionsGranted) {
                Log.d(TAG, "start record")
                mediaProjectionManager?.apply {
                    listener?.onBeforeRecord()
                    val intent = this.createScreenCaptureIntent()
                    if (activity.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                        activity.startActivityForResult(intent, REQUEST_CODE)
                    } else {
                        showToast(R.string.phone_not_support_screen_record)
                    }
                }
            } else {
                showToast(R.string.permission_denied)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun resume() {
        mediaRecorder?.resume()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun pause() {
        mediaRecorder?.pause()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mediaProjection = mediaProjectionManager!!.getMediaProjection(resultCode, data)
                // 实测，部分手机上录制视频的时候会有弹窗的出现
                Handler().postDelayed({
                    if (initRecorder()) {
                        isRecording = true
                        mediaRecorder?.start()
                        listener?.onStartRecord()
                    } else {
                        showToast(R.string.phone_not_support_screen_record)

                    }
                }, 150)
            } else {
                showToast(R.string.phone_not_support_screen_record)
            }
        }
    }

    fun cancelRecord() {
        stopRecord()
        saveFile?.delete()
        saveFile = null
        listener?.onCancelRecord()
    }

    private fun showToast(resId: Int) {
        Toast.makeText(activity.applicationContext, activity.applicationContext.getString(resId), Toast.LENGTH_SHORT).show()
    }

    private fun stop() {
        if (isRecording) {
            isRecording = false
            try {
                mediaRecorder?.apply {
                    setOnErrorListener(null)
                    setOnInfoListener(null)
                    setPreviewDisplay(null)
                    stop()
                    Log.d(TAG, "stop success")
                }
            } catch (e: Exception) {
                Log.e(TAG, "stopRecorder() error！${e.message}")
            } finally {
                mediaRecorder?.reset()
                virtualDisplay?.release()
                mediaProjection?.stop()
                listener?.onEndRecord()
            }


        }
    }

    /**
     * if you has parameters, the recordAudio will be invalid
     */
    fun stopRecord(videoDuration: Long = 0, audioDuration: Long = 0, afdd: AssetFileDescriptor? = null) {
        stop()
        if (audioDuration != 0L && afdd != null) {
            syntheticAudio(videoDuration, audioDuration, afdd)
        } else {
            // saveFile
            if (saveFile != null) {
                val newFile = File(savePath, "$saveName.mp4")
                // 录制结束后修改后缀为 mp4
                saveFile!!.renameTo(newFile)
                refreshVideo(newFile)
            }
            saveFile = null
        }

    }

    private fun refreshVideo(newFile: File) {
        Log.d(TAG, "screen record end,file length:${newFile.length()}.")
        if (newFile.length() > 5000) {
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.fromFile(newFile)
            activity.sendBroadcast(intent)
            showToast(R.string.save_to_album_success)
        } else {
            newFile.delete()
            showToast(R.string.phone_not_support_screen_record)
            Log.d(TAG, activity.getString(R.string.record_faild))
        }
    }

    private fun initRecorder(): Boolean {
        Log.d(TAG, "initRecorder")
        var result = true
        val f = File(savePath)
        if (!f.exists()) {
            f.mkdirs()
        }
        saveFile = File(savePath, "$saveName.tmp")
        saveFile?.apply {
            if (exists()) {
                delete()
            }
        }
        mediaRecorder = MediaRecorder()
        val width = Math.min(displayMetrics.widthPixels, 1080)
        val height = Math.min(displayMetrics.heightPixels, 1920)
        mediaRecorder?.apply {
            if (recordAudio) {
                setAudioSource(MediaRecorder.AudioSource.MIC)
            }
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            if (recordAudio) {
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            }
            setOutputFile(saveFile!!.absolutePath)
            setVideoSize(width, height)
            setVideoEncodingBitRate(8388608)
            setVideoFrameRate(VIDEO_FRAME_RATE)
            try {
                prepare()
                virtualDisplay = mediaProjection?.createVirtualDisplay("MainScreen", width, height, displayMetrics.densityDpi,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, surface, null, null)
                Log.d(TAG, "initRecorder 成功")
            } catch (e: Exception) {
                Log.e(TAG, "IllegalStateException preparing MediaRecorder: ${e.message}")
                e.printStackTrace()
                result = false
            }
        }
        return result
    }

    fun clearAll() {
        mediaRecorder?.release()
        mediaRecorder = null
        virtualDisplay?.release()
        virtualDisplay = null
        mediaProjection?.stop()
        mediaProjection = null
    }


    /**
     * https://stackoverflow.com/questions/31572067/android-how-to-mux-audio-file-and-video-file
     */
    private fun syntheticAudio(audioDuration: Long, videoDuration: Long, afdd: AssetFileDescriptor) {
        Log.d(TAG, "start syntheticAudio")
        val newFile = File(savePath, "$saveName.mp4")
        if (newFile.exists()) {
            newFile.delete()
        }
        try {
            newFile.createNewFile()
            val videoExtractor = MediaExtractor()
            videoExtractor.setDataSource(saveFile!!.absolutePath)
            val audioExtractor = MediaExtractor()
            afdd.apply {
                audioExtractor.setDataSource(fileDescriptor, startOffset, length * videoDuration / audioDuration)
            }
            val muxer = MediaMuxer(newFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
            videoExtractor.selectTrack(0)
            val videoFormat = videoExtractor.getTrackFormat(0)
            val videoTrack = muxer.addTrack(videoFormat)

            audioExtractor.selectTrack(0)
            val audioFormat = audioExtractor.getTrackFormat(0)
            val audioTrack = muxer.addTrack(audioFormat)

            var sawEOS = false
            var frameCount = 0
            val offset = 100
            val sampleSize = 1000 * 1024
            val videoBuf = ByteBuffer.allocate(sampleSize)
            val audioBuf = ByteBuffer.allocate(sampleSize)
            val videoBufferInfo = MediaCodec.BufferInfo()
            val audioBufferInfo = MediaCodec.BufferInfo()

            videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)

            muxer.start()

            // 每秒多少帧
            // 实测 OPPO R9em 垃圾手机，拿出来的没有 MediaFormat.KEY_FRAME_RATE
            val frameRate = if (videoFormat.containsKey(MediaFormat.KEY_FRAME_RATE)) {
                videoFormat.getInteger(MediaFormat.KEY_FRAME_RATE)
            } else {
                31
            }
            // 得出平均每一帧间隔多少微妙
            val videoSampleTime = 1000 * 1000 / frameRate
            while (!sawEOS) {
                videoBufferInfo.offset = offset
                videoBufferInfo.size = videoExtractor.readSampleData(videoBuf, offset)
                if (videoBufferInfo.size < 0) {
                    sawEOS = true
                    videoBufferInfo.size = 0
                } else {
                    videoBufferInfo.presentationTimeUs += videoSampleTime
                    videoBufferInfo.flags = videoExtractor.sampleFlags
                    muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo)
                    videoExtractor.advance()
                    frameCount++
                }
            }
            var sawEOS2 = false
            var frameCount2 = 0
            while (!sawEOS2) {
                frameCount2++
                audioBufferInfo.offset = offset
                audioBufferInfo.size = audioExtractor.readSampleData(audioBuf, offset)

                if (audioBufferInfo.size < 0) {
                    sawEOS2 = true
                    audioBufferInfo.size = 0
                } else {
                    audioBufferInfo.presentationTimeUs = audioExtractor.sampleTime
                    audioBufferInfo.flags = audioExtractor.sampleFlags
                    muxer.writeSampleData(audioTrack, audioBuf, audioBufferInfo)
                    audioExtractor.advance()
                }
            }
            muxer.stop()
            muxer.release()
            videoExtractor.release()
            audioExtractor.release()

            // 删除无声视频文件
            saveFile?.delete()
        } catch (e: Exception) {
            Log.e(TAG, "Mixer Error:${e.message}")
            // 视频添加音频合成失败，直接保存视频
            saveFile?.renameTo(newFile)

        } finally {
            afdd.close()
            Handler().post {
                refreshVideo(newFile)
                saveFile = null
            }
        }
    }

    companion object {
        private const val VIDEO_FRAME_RATE = 30
        private const val REQUEST_CODE = 1024
        private const val TAG = "ScreenRecordHelper"
    }

    interface OnVideoRecordListener {
        /**
         * 录制开始时隐藏不必要的UI
         */
        fun onBeforeRecord()

        /**
         * 开始录制
         */
        fun onStartRecord()

        /**
         * 取消录制
         */
        fun onCancelRecord()

        /**
         * 结束录制
         */
        fun onEndRecord()
    }
}