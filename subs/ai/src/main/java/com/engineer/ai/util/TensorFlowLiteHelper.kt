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
    private lateinit var initializeTask: Task<Void>
    private var interpreter: InterpreterApi? = null

    fun init(context: Context, cb: (Boolean) -> Unit) {
        initializeTask = TfLite.initialize(context)
        initializeTask.addOnSuccessListener {
            Log.d(TAG, "Initialized TFLite interpreter.")
            Log.d(TAG, "ver ${TensorFlowLite.schemaVersion(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)}")
            Log.d(TAG, "ver ${TensorFlowLite.runtimeVersion(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)}")
            cb(true)
        }.addOnFailureListener {
            Log.d(TAG, "Initialized TFLite fail.")
            cb(false)
        }
    }

    fun createInterpreterApi(context: Context, modelName: String): InterpreterApi? {
        val model = loadModelFile(context.assets, modelName)
        val interpreterOption =
            InterpreterApi.Options().setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)
        interpreter = InterpreterApi.create(model, interpreterOption)
        return interpreter
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