package com.engineer.ai

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.engineer.ai.databinding.ActivityTansStyleBinding
import com.engineer.ai.util.AndroidAssetsFileUtil
import com.engineer.ai.util.AsyncExecutor
import com.engineer.ai.util.StyleTransferProcessor
import com.engineer.ai.util.gone
import com.engineer.ai.util.show
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.pytorch.LiteModuleLoader
import org.pytorch.Module


class FastStyleTransActivity : AppCompatActivity() {
    private val TAG = "FastStyleTransActivity_TAG"
    private lateinit var module: Module
    private val modelName = "mosaic.pt"
    private var currentBitmap: Bitmap? = null

    private lateinit var viewBinding: ActivityTansStyleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityTansStyleBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        initModel()
        viewBinding.pickImg.setOnClickListener {
            pickImage()
        }
        viewBinding.gen.setOnClickListener {
            refreshLoading(true)
//            lifecycleScope.launch {
//                genImage()
//            }
            GlobalScope.launch {
                genImage()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel()
    }

    private fun showBitmap(bitmap: Bitmap) {
        viewBinding.pickResult.setImageBitmap(bitmap)

        Log.i(TAG, "ori = ${bitmap.width},${bitmap.height}")
        currentBitmap = bitmap
    }

    private fun refreshLoading(show: Boolean) {
        if (show) viewBinding.loading.show() else viewBinding.loading.gone()
    }


    private fun genImage() {

        currentBitmap?.let {
//            AsyncExecutor.fromIO().execute {
//                StyleTransferProcessor.initModule(module)
//                StyleTransferProcessor.transferStyle(it, 1.0f)
//            }.awaitResult<Bitmap>(onSuccess = {
//                Log.i(TAG, "onSuccess")
//                refreshLoading(false)
//                Log.i(TAG, "output ${it.width},${it.height}")
//                viewBinding.transResult.setImageBitmap(it)
//            }, onError = {
//                refreshLoading(false)
//                Log.i(TAG, it.stackTraceToString())
//            })

            AsyncExecutor.fromIO().execute {
                StyleTransferProcessor.initModule(module)
//                val it = StyleTransferProcessor.transferStyle(it, 1.0f)
//
//                withContext(Dispatchers.Main) {
//                    refreshLoading(false)
//                    Log.i(TAG, "output ${it.width},${it.height}")
//                    viewBinding.transResult.setImageBitmap(it)
//                }

                StyleTransferProcessor.transferStyleAsync(it, 0.5f) {
                    runOnUiThread {
                        refreshLoading(false)

                        viewBinding.transResult.setImageBitmap(it)
                    }
                }
            }
        }


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