package com.engineer.ai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.engineer.ai.databinding.ActivityGanBinding
import org.pytorch.IValue
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

        val z = FloatArray(zDim[0] * outDims[1])
        val random = Random(System.currentTimeMillis())
        z[0] = random.nextGaussian().toFloat()
        val shape = longArrayOf(1, 100)
        val tensor = Tensor.fromBlob(z, shape)

        val resultT = module.forward(IValue.from(tensor)).toTensor()
        val resultArray = resultT.dataAsFloatArray

//        val img = floatArrayOf(outDims[0],outDims[1],outDims[2])

    }

    private fun initModel() {
        module = Module.load(AndroidAssetsFileUtil.assetFilePath(this, modelName))
    }


}