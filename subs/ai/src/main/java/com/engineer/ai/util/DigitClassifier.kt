/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package com.engineer.ai.util

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tflite.java.TfLite
import org.tensorflow.lite.InterpreterApi
import org.tensorflow.lite.TensorFlowLite
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.core.graphics.scale

class DigitClassifier(private val context: Context) {

//    private var interpreter: Interpreter? = null

    var isInitialized = false
        private set

    /** Executor to run inference task in the background. */
    private val executorService: ExecutorService = Executors.newCachedThreadPool()

    private var inputImageWidth: Int = 0 // will be inferred from TF Lite model.
    private var inputImageHeight: Int = 0 // will be inferred from TF Lite model.
    private var modelInputSize: Int = 0 // will be inferred from TF Lite model.

    private val initializeTask: Task<Void> by lazy { TfLite.initialize(context) }
    private var interpreter: InterpreterApi? = null

    fun initialize(cb: (Boolean) -> Unit) {
        val assetManager = context.assets
        val model = loadModelFile(assetManager, "mnist.tflite")

        initializeTask.addOnSuccessListener {
            val interpreterOption =
                InterpreterApi.Options().setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)
            interpreter = InterpreterApi.create(model, interpreterOption)

            Log.d(TAG, "ver ${TensorFlowLite.schemaVersion(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)}")
            Log.d(TAG, "ver ${TensorFlowLite.runtimeVersion(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)}")
            // Read input shape from model file
            interpreter?.let {
                val inputShape = it.getInputTensor(0).shape()
                inputImageWidth = inputShape[1]
                inputImageHeight = inputShape[2]
                modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE


                isInitialized = true
                Log.d(TAG, "Initialized TFLite interpreter.")
                cb(true)
            } ?: run {
                Log.d(TAG, "Initialized TFLite fail.")
            }

        }.addOnFailureListener { e ->
            cb(false)
            Log.e(TAG, "Cannot initialize interpreter", e)
        }


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

    private fun classify(bitmap: Bitmap): String {
        check(isInitialized) { "TF Lite Interpreter is not initialized yet." }


        // Preprocessing: resize the input image to match the model input shape.
        val resizedImage = bitmap.scale(inputImageWidth, inputImageHeight)
        val byteBuffer = convertBitmapToByteBuffer(resizedImage)
        // Define an array to store the model output.
        val output = Array(1) { FloatArray(OUTPUT_CLASSES_COUNT) }

        // Run inference with the input data.
        interpreter?.run(byteBuffer, output)
        // Post-processing: find the digit that has the highest probability
        // and return it a human-readable string.
        val result = output[0]
        val maxIndex = result.indices.maxBy { result[it] }
        val resultString = "Prediction Result: %d\nConfidence: %2f".format(maxIndex, result[maxIndex])

        return resultString

        //        return "Let's add TensorFlow Lite code!"
    }

    fun classifyAsync(bitmap: Bitmap): Task<String> {
        val task = TaskCompletionSource<String>()
        executorService.execute {
            val result = classify(bitmap)
            task.setResult(result)
        }
        return task.task
    }

    fun close() {
        executorService.execute {
            interpreter?.close()
            Log.d(TAG, "Closed TFLite interpreter.")
        }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageWidth * inputImageHeight)
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

    companion object {
        private const val TAG = "DigitClassifier"

        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 1

        private const val OUTPUT_CLASSES_COUNT = 10
    }
}
