package com.engineer.ai.util

import android.content.Context
import android.view.View
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

object AndroidAssetsFileUtil {


    fun assetFilePath(context: Context, assetName: String): String {
        val file = File(context.filesDir, assetName)
        if (file.exists() && file.length() > 0) {
            return file.absolutePath
        }

        return context.assets.open(assetName).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }
            file.absolutePath
        }
    }
}

fun View?.show() {
    this?.let {
        visibility = View.VISIBLE
    }
}

fun View?.gone() {
    this?.let {
        visibility = View.GONE
    }
}