package com.engineer.ai.util

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import org.pytorch.IValue
import org.pytorch.Module
import org.pytorch.Tensor
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

object StyleTransferProcessor {
    private const val TAG = "StyleTransferProcessor"

    private lateinit var module: Module

    fun initModule(m: Module) {
        this.module = m
    }

    private fun preprocessImage(bitmap: Bitmap, scale: Float): Triple<FloatBuffer, Int, Int> {
        Log.d(TAG, "preprocessImage() called with: bitmap = $bitmap, scale = $scale")
        // Resize the image if needed
        val scaledBitmap = if (scale != 1.0f) {
            bitmap.scale((bitmap.width * scale).toInt(), (bitmap.height * scale).toInt())
        } else {
            bitmap
        }

        Log.i(TAG, "scale ${scaledBitmap.width},${scaledBitmap.height}")

        val width = scaledBitmap.width
        val height = scaledBitmap.height
        val channels = 3
        val totalPixels = width * height

        // Create DIRECT float buffer
        val byteBuffer =
            ByteBuffer.allocateDirect(channels * width * height * 4).order(ByteOrder.nativeOrder())
        val floatBuffer = byteBuffer.asFloatBuffer()

        // Get all pixels at once for better performance
        val pixels = IntArray(width * height)
        scaledBitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        // Convert to CHW format and scale by 255
        for (i in 0 until totalPixels) {
            val pixel = pixels[i]
            // RGB channels (ignore alpha)
            floatBuffer.put(i, ((pixel shr 16) and 0xFF).toFloat()) // R
            floatBuffer.put(totalPixels + i, ((pixel shr 8) and 0xFF).toFloat()) // G
            floatBuffer.put(2 * totalPixels + i, (pixel and 0xFF).toFloat()) // B
        }

        floatBuffer.rewind()
        return Triple(floatBuffer, width, height)
    }

    private fun postprocessingImage(
        outputTensor: Tensor, width: Int, height: Int, cb: ((Bitmap) -> Unit)? = null
    ): Bitmap {
        Log.d(
            TAG,
            "postprocessingImage() called with: outputTensor = $outputTensor, width = $width, height = $height"
        )
        // Get the output data
        val outputData = outputTensor.dataAsFloatArray

        // Create output bitmap
        val outputBitmap = createBitmap(width, height)
//        outputBitmap.eraseColor(Color.TRANSPARENT)
        val pixels = IntArray(width * height)

        // Convert from CHW to ARGB format
        val channelSize = width * height
        Log.d(TAG, "channelSize = $channelSize")
        for (i in 0 until channelSize) {
            // Get RGB values (scaled back from 0-255)
            val r = outputData[i].toInt().coerceIn(0, 255)
            val g = outputData[i + channelSize].toInt().coerceIn(0, 255)
            val b = outputData[i + 2 * channelSize].toInt().coerceIn(0, 255)

            // Combine into ARGB pixel
            pixels[i] = 0xFF shl 24 or (r shl 16) or (g shl 8) or b
//            Log.d(TAG,"i = $i,pixel = ${pixels[i]}")
            if (i % width == 0) {
                val h = i / width
                outputBitmap.setPixels(pixels, 0, width, 0, 0, width, h)
                cb?.invoke(outputBitmap)
            }
        }

        // Set pixels to bitmap
        outputBitmap.setPixels(pixels, 0, width, 0, 0, width, height)

        Log.d(TAG, "bitmap is ok")
        return outputBitmap
    }

    fun transferStyleAsync(
        contentImage: Bitmap, scale: Float = 1.0f, cb: ((Bitmap) -> Unit)? = null
    ): Bitmap {
        Log.d(TAG, "transferStyle() called with: contentImage = $contentImage, scale = $scale")
        // 1. Preprocess the content image (simple ToTensor + multiply by 255)
        val (transformedImage, width, height) = preprocessImage(contentImage, scale)

        // 2. Create input tensor
        val inputTensor = Tensor.fromBlob(
            transformedImage, longArrayOf(1, 3, height.toLong(), width.toLong())
        )
        Log.d(TAG, "transferStyle() step2 done")

        // 3. Run the model
        val outputTensor = module.forward(IValue.from(inputTensor)).toTensor()

        Log.d(TAG, "transferStyle() step3 done")

        // 4. Postprocessor the output
        return postprocessingImage(outputTensor, width, height, cb)
    }

    fun transferStyle(contentImage: Bitmap, scale: Float = 1.0f): Bitmap {
        Log.d(TAG, "transferStyle() called with: contentImage = $contentImage, scale = $scale")
        // 1. Preprocess the content image (simple ToTensor + multiply by 255)
        val (transformedImage, width, height) = preprocessImage(contentImage, scale)

        // 2. Create input tensor
        val inputTensor = Tensor.fromBlob(
            transformedImage, longArrayOf(1, 3, height.toLong(), width.toLong())
        )
        Log.d(TAG, "transferStyle() step2 done")

        // 3. Run the model
        val outputTensor = module.forward(IValue.from(inputTensor)).toTensor()

        Log.d(TAG, "transferStyle() step3 done")

        // 4. Postprocessor the output
        return postprocessingImage(outputTensor, width, height)
//        return postprocessImage(outputTensor, width, height)
    }

}