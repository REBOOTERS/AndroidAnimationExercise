package com.engineer.imitate.util

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpp_native.internal.PatchUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtil
import java.io.File
import java.io.FileOutputStream

private const val TAG = "PatchPlayground"

private const val PATCH = "patch"

class PatchViewModel(val app: Application) : AndroidViewModel(app) {

    fun copyFile() {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e(TAG, "exceptionHandler: " + throwable.stackTraceToString())
        }
        viewModelScope.launch(exceptionHandler) {
            if (prepare(app)) {
                Log.d(TAG, "copy success start merge")
                if (mergeFile(app)) {
                    Log.d(TAG, "merge success")
                }
            }
        }
    }
}

suspend fun prepare(context: Context): Boolean {
    val baseDir = context.cacheDir.absolutePath + File.separator + PATCH + File.separator
    withContext(Dispatchers.IO) {
        val targetDir = File(baseDir)
        Log.i(TAG, "prepare: $targetDir")
        if (targetDir.exists().not()) {
            targetDir.mkdir()
        }
        val result = baseDir + "result.txt"
        val resultFile = File(result)
        if (resultFile.exists()) {
            Log.i(TAG, "delete resultFile " + resultFile.delete())
        }

        val patchDir = context.assets.list(PATCH)
        patchDir?.forEach {
            val f = PATCH + File.separator + it
            Log.i(TAG, "copy file $f")
            val input = context.assets.open(f)
            val fileOut = targetDir.absolutePath + File.separator + it
            val outputStream = FileOutputStream(fileOut)
            IOUtil.copy(input, outputStream)
        }
        val file = File(baseDir)
        if (file.isDirectory) {
            val list = file.listFiles()
            list.forEach { l ->
                Log.i(TAG, "fileName: ${l.absolutePath} ")
            }
        }
    }
    return File(baseDir).listFiles()?.size ?: 0 >= 2

}

suspend fun mergeFile(context: Context): Boolean {
    val baseDir = context.cacheDir.absolutePath + File.separator + PATCH + File.separator

    val result = baseDir + "result.txt"
    val oldFile = baseDir + "lastest.txt"
    val patchFile = baseDir + "diff.patch"
    withContext(Dispatchers.IO) {
        PatchUtil.patchAPK(oldFile, result, patchFile)
    }

    val mergeResult = FileUtils.fileRead(result)
    Log.e(TAG, "mergeFile() called with: mergeResult = $mergeResult")
    Log.e(TAG, "mergeFile() called with: ${"123456789" == mergeResult.trim()}")
    return "123456789" == mergeResult.trim()
}