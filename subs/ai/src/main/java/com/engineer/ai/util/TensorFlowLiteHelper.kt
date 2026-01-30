package com.engineer.ai.util

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tflite.java.TfLite
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.TensorFlowLite
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

object TensorFlowLiteHelper {
    private const val TAG = "TensorFlowLiteHelper"

    /**
     * Try to init Google Play Services TFLite (dynamite module).
     *
     * This will fail on devices without Google Play Services (e.g. many CN ROMs).
     * We treat failure as non-fatal and fall back to bundled TFLite runtime.
     */
    fun init(context: Context, cb: (Boolean) -> Unit) {
        TfLite.initialize(context)
            .addOnSuccessListener {
                Log.d(TAG, "Initialized Play Services TFLite.")
                try {
                    Log.d(
                        TAG,
                        "schema=${TensorFlowLite.schemaVersion(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)} runtime=${TensorFlowLite.runtimeVersion(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)}"
                    )
                } catch (t: Throwable) {
                    Log.w(TAG, "Unable to query system-only TFLite version.", t)
                }
                cb(true)
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Play Services TFLite init failed; will fall back to bundled runtime.", e)
                cb(false)
            }
    }

    fun createInterpreterApi(context: Context, modelName: String, preferPlayServices: Boolean): InterpreterApi {
        val model = loadModelFile(context.assets, modelName)

        val runtime = if (preferPlayServices) {
            InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY
        } else {
            // Bundled runtime provided by org.tensorflow:tensorflow-lite
            InterpreterApi.Options.TfLiteRuntime.FROM_APPLICATION_ONLY
        }

        val options = InterpreterApi.Options().setRuntime(runtime)
        return InterpreterApi.create(model, options)
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, filename: String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun convertBitmapToByteBuffer(config: Config, bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(config.modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(config.inputImageWidth * config.inputImageHeight)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF)
            val g = (pixelValue shr 8 and 0xFF)
            val b = (pixelValue and 0xFF)

            // Convert RGB to grayscale and normalize pixel value to [0..1].
            val normalizedPixelValue = (r + g + b) / 3.0f / 255.0f
            byteBuffer.putFloat(normalizedPixelValue)
        }

        return byteBuffer
    }

    data class Config(var modelInputSize: Int, var inputImageWidth: Int, var inputImageHeight: Int)

}