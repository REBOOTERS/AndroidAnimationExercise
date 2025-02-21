package com.engineer.ai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.engineer.ai.databinding.ActivityGanBinding
import com.engineer.ai.util.AndroidAssetsFileUtil
import com.engineer.ai.util.Utils
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.Tensor
import java.util.Random


class GanActivity : AppCompatActivity() {
    private lateinit var module: Module
    private val modelName = "dcgan.pt"

    private lateinit var viewBinding: ActivityGanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGanBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initModel()
        viewBinding.gen.setOnClickListener {
            genImage()
        }
    }

    private fun genImage() {
        val zDim = intArrayOf(1, 100)
        val outDims = intArrayOf(64, 64, 3)

        val z = FloatArray(zDim[0] * zDim[1])

        val rand = Random()
        // 生成高斯随机数
        for (c in 0 until zDim[0] * zDim[1]) {
            z[c] = rand.nextGaussian().toFloat()
        }
        val shape = longArrayOf(1, 100)
        val tensor = Tensor.fromBlob(z, shape)

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
        val bitmap = Utils.getBitmap(resultImg, outDims)
        viewBinding.ganResult.setImageBitmap(bitmap)
    }

    private fun initModel() {
        module = LiteModuleLoader.load(AndroidAssetsFileUtil.assetFilePath(this, modelName))
    }


}