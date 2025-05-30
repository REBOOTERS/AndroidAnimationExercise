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
import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.scale
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import org.tensorflow.lite.InterpreterApi
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DigitClassifier(private val context: Context) {

//    private var interpreter: Interpreter? = null

    var isInitialized = false
        private set

    /** Executor to run inference task in the background. */
    private val executorService: ExecutorService = Executors.newCachedThreadPool()

    private var inputImageWidth: Int = 0 // will be inferred from TF Lite model.
    private var inputImageHeight: Int = 0 // will be inferred from TF Lite model.
    private var modelInputSize: Int = 0 // will be inferred from TF Lite model.

    private var interpreter: InterpreterApi? = null

    fun initialize(cb: (Boolean) -> Unit) {
        TensorFlowLiteHelper.init(context) {
            cb(it)
            if (it) {
                interpreter = TensorFlowLiteHelper.createInterpreterApi(context, "mnist.tflite")
                // Read input shape from model file
                interpreter?.let { inter ->
                    val inputShape = inter.getInputTensor(0).shape()
                    inputImageWidth = inputShape[1]
                    inputImageHeight = inputShape[2]
                    modelInputSize = FLOAT_TYPE_SIZE * inputImageWidth * inputImageHeight * PIXEL_SIZE
                    isInitialized = true
                }
            }
        }
    }


    private fun classify(bitmap: Bitmap): String {
        check(isInitialized) { "TF Lite Interpreter is not initialized yet." }
        // Preprocessing: resize the input image to match the model input shape.
        val resizedImage = bitmap.scale(inputImageWidth, inputImageHeight)
        val config = TensorFlowLiteHelper.Config(modelInputSize, inputImageWidth, inputImageHeight)
        val byteBuffer = TensorFlowLiteHelper.convertBitmapToByteBuffer(config, resizedImage)

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


    companion object {
        private const val TAG = "DigitClassifier"

        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 1

        private const val OUTPUT_CLASSES_COUNT = 10
    }
}
