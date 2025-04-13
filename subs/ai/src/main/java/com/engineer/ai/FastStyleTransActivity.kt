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
import com.engineer.ai.databinding.ActivityTansStyleBinding
import com.engineer.ai.util.AndroidAssetsFileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.Tensor
import org.pytorch.torchvision.TensorImageUtils


class FastStyleTransActivity : AppCompatActivity() {
    private val TAG = "FastStyleTransActivity"
    private lateinit var module: Module
    private val modelName = "mosaic.pt"
    private var currentBitmap: Bitmap? = null

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
            GlobalScope.launch(Dispatchers.IO) {
                genImage()
            }
        }
    }

    private fun showBitmap(bitmap: Bitmap) {
        viewBinding.pickResult.setImageBitmap(bitmap)
        currentBitmap = bitmap
    }

    fun multiplyTensorBy255(inputTensor: Tensor): Tensor {
        // 获取 Tensor 的浮点数组
        val inputArray = inputTensor.dataAsFloatArray

        // 创建新数组并乘以255
        val outputArray = FloatArray(inputArray.size) { i ->
            inputArray[i] * 255.0f
        }

        // 创建新的 Tensor（保持原始形状）
        return Tensor.fromBlob(outputArray, inputTensor.shape())
    }

    fun divTensorBy255(inputTensor: Tensor): Tensor {
        // 获取 Tensor 的浮点数组
        val inputArray = inputTensor.dataAsFloatArray

        // 创建新数组并乘以255
        val outputArray = FloatArray(inputArray.size) { i ->
            inputArray[i] * 255.0f
        }

        // 创建新的 Tensor（保持原始形状）
        return Tensor.fromBlob(outputArray, inputTensor.shape())
    }

    private fun genImage() {

        val inDims: IntArray = intArrayOf(224, 224, 3)
        val outDims: IntArray = intArrayOf(224, 224, 3)
        val bmp: Bitmap? = null
        var scaledBmp: Bitmap? = null
        val filePath = ""
        currentBitmap?.let {
            scaledBmp = Bitmap.createScaledBitmap(it, inDims[0], inDims[1], true);


            // Android更简洁的实现
            // 转换为张量并归一化到[0,1]
            val inputTensor: Tensor = TensorImageUtils.bitmapToFloat32Tensor(
                currentBitmap, floatArrayOf(0f, 0f, 0f),  // 不减去均值
                floatArrayOf(1f, 1f, 1f)   // 不除以标准差
            )

            val tensor = multiplyTensorBy255(inputTensor);

            Log.i(TAG, "1")

            val resultTensor = module.forward(IValue.from(tensor)).toTensor()
            val out = divTensorBy255(resultTensor)

            Log.i(TAG, "2")

            val outputArray = out.dataAsFloatArray
            val width = outDims[0]
            val height = outDims[1]
            val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            // 将浮点数组转换为Bitmap (简化实现，实际可能需要更复杂的转换)
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val r = (outputArray[y * width * 3 + x * 3 + 0] * 255).toInt().coerceIn(0, 255)
                    val g = (outputArray[y * width * 3 + x * 3 + 1] * 255).toInt().coerceIn(0, 255)
                    val b = (outputArray[y * width * 3 + x * 3 + 2] * 255).toInt().coerceIn(0, 255)
                    outputBitmap.setPixel(x, y, android.graphics.Color.rgb(r, g, b))
                }
            }
            Log.i(TAG, "3")
            GlobalScope.launch(Dispatchers.Main) {
                viewBinding.transResult.setImageBitmap(outputBitmap)
            }
        }

//        val zDim = intArrayOf(1, 100)
//        val outDims = intArrayOf(64, 64, 3)
//        Log.d(TAG, zDim.contentToString())
//        val z = FloatArray(zDim[0] * zDim[1])
//        Log.d(TAG, "z = ${z.contentToString()}")
//        val rand = Random()
//        // 生成高斯随机数
//        for (c in 0 until zDim[0] * zDim[1]) {
//            z[c] = rand.nextGaussian().toFloat()
//        }
//        Log.d(TAG, "z = ${z.contentToString()}")
//        val shape = longArrayOf(1, 100)
//        val tensor = Tensor.fromBlob(z, shape)
//        Log.d(TAG, tensor.dataAsFloatArray.contentToString())
//        val resultT = module.forward(IValue.from(tensor)).toTensor()
//        val resultArray = resultT.dataAsFloatArray
//        val resultImg = Array(outDims[0]) { Array(outDims[1]) { FloatArray(outDims[2]) { 0.0f } } }
//        var index = 0
//        // 根据输出的一维数组，解析生成的卡通图像
//        for (j in 0 until outDims[2]) {
//            for (k in 0 until outDims[0]) {
//                for (m in 0 until outDims[1]) {
//                    resultImg[k][m][j] = resultArray[index] * 127.5f + 127.5f
//                    index++
//                }
//            }
//        }
//        val bitmap = Utils.getBitmap(resultImg, outDims)
//        viewBinding.transResult.setImageBitmap(bitmap)
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

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
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
                AlertDialog.Builder(this).setTitle("需要权限")
                    .setMessage("需要存储权限才能从相册选择图片")
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