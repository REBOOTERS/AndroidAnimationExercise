package com.engineer.gif.revert.internal

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * @author rookie
 * @since 07-06-2019
 *
 * 文件 IO 流操作相关
 */

internal object IOTool {

    const val gif = "gif"
    // "/gif/"
    val gifDir = File.separator + gif + File.separator

    /**
     * 保存 Bitmap 到 /data/user/0/package_name/files 目录下
     *
     * 返回保存路径
     */
    fun saveBitmap2Box(context: Context, bitmap: Bitmap?, name: String): String {
        val result: String
        if (bitmap == null) {
            return ""
        }
        val path = context.filesDir.absolutePath + gifDir
        val fileDir = File(path)
        if (fileDir.exists().not()) {
            fileDir.mkdir()
        }
        val fileName = "$name.jpg"
        val file = File(fileDir, fileName)
        val fileOutPutStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutPutStream)
        fileOutPutStream.flush()
        result = file.absolutePath

        return result
    }

    fun provideRandomPath(name: String): String {
        val picturesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val path = picturesDir.absolutePath + gifDir
        val fileDir = File(path)
        if (fileDir.exists().not()) {
            fileDir.mkdir()
        }
        val fileName = name + System.currentTimeMillis() + ".gif"
        val file = File(fileDir, fileName)
        return file.absolutePath
    }

    fun saveStreamToSDCard(name: String, os: ByteArrayOutputStream): String {
        val picturesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val path = picturesDir.absolutePath + gifDir
        val fileDir = File(path)
        if (fileDir.exists().not()) {
            fileDir.mkdir()
        }
        val fileName = name + System.currentTimeMillis() + ".gif"
        val file = File(fileDir, fileName)
        val fileOutPutStream = FileOutputStream(file)


        os.writeTo(fileOutPutStream)
        os.flush()
        fileOutPutStream.flush()
        os.close()
        fileOutPutStream.close()

        return file.absolutePath
    }

    /**
     * 通知一下系统相册，避免生成的图片找到了
     */
    fun notifySystemGallery(context: Context, path: String) {
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val file = File(path)
        val uri = Uri.fromFile(file)
        intent.data = uri
        context.sendBroadcast(intent)
    }
}