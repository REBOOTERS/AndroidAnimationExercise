package com.engineer.ai

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.engineer.ai.databinding.ActivityGanBinding
import com.engineer.ai.databinding.ActivityTansStyleBinding
import com.engineer.ai.util.AndroidAssetsFileUtil
import com.engineer.ai.util.Utils
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.Tensor
import java.util.Random


class FastStyleTransActivity : AppCompatActivity() {
    private val TAG = "FastStyleTransActivity"
    private lateinit var module: Module
    private val modelName = "dcgan.pt"

    private lateinit var viewBinding: ActivityTansStyleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityTansStyleBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initModel()
        viewBinding.pickImg.setOnClickListener {
            pickImage()
        }
        viewBinding.gen.setOnClickListener {
            genImage()
        }
    }

    private fun showBitmap(bitmap: Bitmap) {
        viewBinding.pickResult.setImageBitmap(bitmap)
    }

    private fun genImage() {
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
        val bitmap = Utils.getBitmap(resultImg, outDims)
        viewBinding.transResult.setImageBitmap(bitmap)
    }

    private fun initModel() {
        module = LiteModuleLoader.load(AndroidAssetsFileUtil.assetFilePath(this, modelName))
    }


    fun pickImage() {
        // 检查设备是否支持照片选择器
        if (isPhotoPickerAvailable(this)) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            // 回退到传统方法
            checkAndRequestPermission()
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // 处理选择的图片
        uri?.let {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            showBitmap(bitmap)
        }
    }

    // 定义权限请求和图片选择启动器
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 权限已授予，打开相册
            openGallery()
        } else {
            // 权限被拒绝，显示提示
            Toast.makeText(this, "需要存储权限才能选择图片", Toast.LENGTH_SHORT).show()
        }
    }

    // 图片选择启动器
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        // 处理选择的图片
        uri?.let {
            try {
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                showBitmap(bitmap)
                // 显示选择的图片
//                imageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "无法加载图片", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAndRequestPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // 已经有权限，直接打开相册
                openGallery()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, permission
            ) -> {
                // 解释为什么需要权限
                AlertDialog.Builder(this).setTitle("需要权限").setMessage("需要存储权限才能从相册选择图片")
                    .setPositiveButton("确定") { _, _ ->
                        requestPermissionLauncher.launch(permission)
                    }.setNegativeButton("取消", null).show()
            }

            else -> {
                // 直接请求权限
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    // 打开相册选择图片
    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }
}