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
import com.engineer.ai.util.AndroidAssetsFileUtil
import com.engineer.ai.util.AsyncExecutor
import com.engineer.ai.util.BitmapGridAdapter
import com.engineer.ai.util.Utils
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils
import java.util.Random


class GanActivity : AppCompatActivity() {
    private val TAG = "GanActivity"
    private lateinit var module: Module
    private val modelName = "dcgan.pt"
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
                for (i in 0 until 120) {
                    val bitmap = genImage()
                    bitmapList.add(bitmap)
                }
                Log.i(TAG, "done")
                runOnUiThread {
                    Log.i(TAG, "notify")
                    adapter.notifyDataSetChanged()
                }
            }
        }


        val recyclerView = viewBinding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 6) // 每行 5 列
        adapter = BitmapGridAdapter(bitmapList)
        recyclerView.adapter = adapter
    }

    private fun genImage(): Bitmap {
        val zDim = intArrayOf(1, 100)
        val outDims = intArrayOf(64, 64, 3)
        Log.d(TAG, zDim.contentToString())
        val z = FloatArray(zDim[0] * zDim[1])
        Log.d(TAG, "z = ${z.contentToString()}")
        val rand = Random()
        // 生成高斯随机数
        for (c in 0 until zDim[0] * zDim[1]) {
            z[c] = rand.nextGaussian().toFloat()
        }
        Log.d(TAG, "z = ${z.contentToString()}")
        val shape = longArrayOf(1, 100)
        val tensor = Tensor.fromBlob(z, shape)
        Log.d(TAG, tensor.dataAsFloatArray.contentToString())
        val resultT = module.forward(IValue.from(tensor)).toTensor()
        val resultArray = resultT.dataAsFloatArray
        val resultImg = Array(outDims[0]) { Array(outDims[1]) { FloatArray(outDims[2]) { 0.0f } } }
        var index = 0
        // 根据输出的一维数组，解析生成的卡通图像
        for (j in 0 until outDims[2]) {
            for (k in 0 until outDims[0]) {
                for (m in 0 until outDims[1]) {
                    resultImg[k][m][j] = resultArray[index] * 127.5f + 127.5f
                    index++
                }
            }
        }
        Log.e(TAG, "${resultImg.size}, ${resultImg[0].size},${resultImg[0][0].contentToString()}")
        Log.e(TAG, "${resultImg.size}, ${resultImg[0].contentToString()}")
        val bitmap = Utils.getBitmap(resultImg, outDims)
        return bitmap
    }

    private fun initModel() {
        module = LiteModuleLoader.load(AndroidAssetsFileUtil.assetFilePath(this, modelName))
    }


}