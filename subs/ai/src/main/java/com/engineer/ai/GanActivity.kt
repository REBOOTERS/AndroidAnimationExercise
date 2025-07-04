package com.engineer.ai

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.engineer.ai.databinding.ActivityGanBinding
import com.engineer.ai.util.AsyncExecutor
import com.engineer.ai.util.BitmapGridAdapter
import com.engineer.ai.util.TensorFlowLiteHelper
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.Random


class GanActivity : AppCompatActivity() {
    private val TAG = "GanActivity"

    private val bitmapList = mutableListOf<Bitmap>()
    private lateinit var adapter: BitmapGridAdapter

    private lateinit var viewBinding: ActivityGanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowInsetsControllerCompat(
            window, window.decorView
        ).hide(WindowInsetsCompat.Type.statusBars())
        viewBinding = ActivityGanBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initModel()
        viewBinding.gen.setOnClickListener {
//            val bitmap = genImage()
//            viewBinding.ganResult.setImageBitmap(bitmap)

            AsyncExecutor.fromIO().execute {
                bitmapList.clear()
//                for (i in 0 until 120) {
//                    val bitmap = genImage()
//                    bitmapList.add(bitmap)
//                }
                Log.i(TAG, "done")
                runOnUiThread {
                    Log.i(TAG, "notify")
                    adapter.notifyDataSetChanged()
                    genBitmap()
                }
            }
        }


        val recyclerView = viewBinding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 6) // 每行 5 列
        adapter = BitmapGridAdapter(bitmapList)
        recyclerView.adapter = adapter
    }

    private fun genBitmap() {
        TensorFlowLiteHelper.init(this) {
            val interpreterApi = TensorFlowLiteHelper.createInterpreterApi(this, "dcgan.tflite")
            interpreterApi?.let {
                Log.d(TAG, interpreterApi.getInputTensor(0).shape().contentToString())
                Log.d(TAG, interpreterApi.getOutputTensor(0).shape().contentToString())


                val noise = generateNoise(100)

                // 2. 创建输入张量
                val inputBuffer =
                    TensorBuffer.createFixedSize(intArrayOf(1, 100), DataType.FLOAT32).apply {
                        loadArray(noise)
                    }

                // 3. 准备输出张量 [1, 3, 64, 64]
                val outputBuffer =
                    TensorBuffer.createFixedSize(intArrayOf(1, 3, 64, 64), DataType.FLOAT32)
                it.run(inputBuffer, outputBuffer)
                Log.d(TAG, outputBuffer.floatArray.contentToString())

                val bitmap = convertOutputToBitmap(outputBuffer.floatArray, 64, 64)
                bitmapList.clear()
                bitmapList.add(bitmap)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun generateNoise(size: Int): FloatArray {
        return FloatArray(size).apply {
            val random = Random()
            for (i in indices) {
                this[i] = random.nextFloat() * 2 - 1 // [-1, 1] 范围
            }
        }
    }

    private fun convertOutputToBitmap(imageData: FloatArray, width: Int, height: Int): Bitmap {
        val pixels = IntArray(width * height)

        // 假设输出是 [1, 3, 64, 64] 格式 (CHW)
        for (y in 0 until height) {
            for (x in 0 until width) {
                // 获取 RGB 值 (范围 [-1, 1] 或 [0, 1] 取决于模型训练时的归一化)
                val r = (imageData[0 * width * height + y * width + x] + 1) * 127.5f // 假设 [-1,1] 范围
                val g = (imageData[1 * width * height + y * width + x] + 1) * 127.5f
                val b = (imageData[2 * width * height + y * width + x] + 1) * 127.5f

                // 确保值在 0-255 范围内
                val red = r.coerceIn(0f, 255f).toInt()
                val green = g.coerceIn(0f, 255f).toInt()
                val blue = b.coerceIn(0f, 255f).toInt()

                // 转换为 ARGB 格式
                pixels[y * width + x] = 0xFF000000.toInt() or (red shl 16) or (green shl 8) or blue
            }
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }

    private fun genImage(): Bitmap? {
        return null
    }

    private fun initModel() {

    }


}