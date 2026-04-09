package com.engineer.ai.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.ai.edge.litert.Accelerator
import com.google.ai.edge.litert.CompiledModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * https://ai.google.dev/edge/litert/android?hl=zh-cn
 *
 * CompiledModel API：高性能推理的现代标准，可简化 CPU/GPU/NPU 之间的硬件加速。
 * 详细了解为何选择 CompiledModel API。https://ai.google.dev/edge/litert/inference?hl=zh-cn
 */
class LiteTRCompileModel(private val context: Context) {

    var isInitialized = false
        private set
    private var model: CompiledModel? = null

    /** Executor to run inference task in the background. */
    private val executorService: ExecutorService = Executors.newCachedThreadPool()

    companion object {
        private const val TAG = "LiteTRCompileModel"

        fun toAccelerator(acceleratorEnum: AcceleratorEnum): Accelerator {
            return when (acceleratorEnum) {
                AcceleratorEnum.CPU -> Accelerator.CPU
                AcceleratorEnum.GPU -> Accelerator.GPU
            }
        }
    }

    enum class AcceleratorEnum {
        CPU, GPU,
    }

    fun initClassifier(acceleratorEnum: AcceleratorEnum = AcceleratorEnum.CPU) {
        cleanup()
        try {

            model = CompiledModel.create(
                context.assets,
                "mnist_metadata.tflite",
                CompiledModel.Options(toAccelerator(acceleratorEnum)),
                null
            )
            isInitialized = true
            Log.i(TAG, "Created a CompiledModel with $acceleratorEnum")

        } catch (e: Exception) {
            Log.e(TAG, "Initializing CompiledModel has failed with error: ${e.message}")
        }
    }

    fun cleanup() {
        model?.close()
        model = null
    }

    fun classify(bitmap: Bitmap): String {

        val localModel = model ?: return ""

        try {
            // 1. Preprocessing
            // Resize to 28x28
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 28, 28, true)
            // Convert to grayscale and normalize to 0..1
            val inputFloatArray = convertBitmapToFloatArray(scaledBitmap)

            // 2. Execution
            val inputBuffers = localModel.createInputBuffers()
            val outputBuffers = localModel.createOutputBuffers()

            inputBuffers[0].writeFloat(inputFloatArray)
            localModel.run(inputBuffers, outputBuffers)

            val outputFloatArray = outputBuffers[0].readFloat()

            // Cleanup buffers
            inputBuffers.forEach { it.close() }
            outputBuffers.forEach { it.close() }

            // 3. Postprocessing
            val (digit, score) = findResult(outputFloatArray)

            return "Prediction Result: %d\nConfidence: %2f".format(digit, score)
        } catch (e: Exception) {
            Log.e(TAG, "Error during classification: ${e.message}")
            return "Classification failed: ${e.message}"
        }
        return ""

    }


    private fun convertBitmapToFloatArray(bitmap: Bitmap): FloatArray {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        // The original sample used ImageProcessor without grayscale conversion,
        // implying it sent 3 channels (RGB) to the model.
        // It also used NormalizeOp(0f, 1f), which effectively keeps the 0-255 range
        // when converting from the default Bitmap uint8 values to Float32.

        // Target: [1, 28, 28, 3]
        val output = FloatArray(width * height * 3)

        for (i in pixels.indices) {
            val pixel = pixels[i]

            // Extract RGB (ignore alpha)
            val r = Color.red(pixel).toFloat()
            val g = Color.green(pixel).toFloat()
            val b = Color.blue(pixel).toFloat()

            // Don't divide by 255.0f to match original 0..255 range behavior
            val baseIndex = i * 3
            output[baseIndex] = r
            output[baseIndex + 1] = g
            output[baseIndex + 2] = b
        }
        return output
    }

    /**
     * Finds the index and value of the maximum element in a non-empty float array.
     */
    private fun findResult(array: FloatArray): Pair<Int, Float> {
        if (array.isEmpty()) return Pair(-1, 0f)

        var maxIndex = 0
        var maxValue = array[0]

        for (i in array.indices) {
            if (array[i] > maxValue) {
                maxValue = array[i]
                maxIndex = i
            }
        }

        return Pair(maxIndex, maxValue)
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
        executorService.shutdown()
    }
}