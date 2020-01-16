package com.engineer.plugin.transforms

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.build.gradle.internal.pipeline.TransformTask
import com.android.builder.utils.isValidZipEntryName
import com.android.utils.FileUtils
import com.engineer.plugin.utils.BeautyLog
import com.google.common.io.Files
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import org.gradle.internal.io.IoUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import java.io.*
import java.util.function.BiConsumer
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * @author rookie
 * @since 12-03-2019
 *
 * 支持 incremental 的 transform
 */
abstract class BaseTransform(private val project: Project) : Transform() {



    // 将对 class 文件的 asm 处理，处理完之后的再次复制，抽象为一个 BiConsumer
    abstract fun provideFunction(): BiConsumer<InputStream, OutputStream>?

    // 默认的 class 过滤器，处理 .class 结尾的所有内容 (maybe 可以扩展)
    private fun classFilter(className: String): Boolean {
        return className.endsWith(SdkConstants.DOT_CLASS)
    }

    // Transform 使能开关
    open fun isEnabled() = true

    // <editor-fold defaultstate="collapsed" desc="basic function">
    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return true
    }
    // </editor-fold>

    override fun transform(transformInvocation: TransformInvocation?) {
        BeautyLog.log(name, true)
        val start = System.nanoTime()
        super.transform(transformInvocation)

        val function = provideFunction()

        if (function == null) {
            println("the function is null")
            return
        }

        val outputProvider: TransformOutputProvider = transformInvocation!!.outputProvider

        if (transformInvocation.isIncremental.not()) {
            outputProvider.deleteAll()
        }

        for (ti in transformInvocation.inputs) {
            for (jarInput in ti.jarInputs) {
                val inputJar = jarInput.file
                val outputJar = outputProvider.getContentLocation(
                    jarInput.name,
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                )
                if (transformInvocation.isIncremental) {
                    when (jarInput.status ?: Status.NOTCHANGED) {
                        Status.NOTCHANGED -> {
                        }
                        Status.ADDED, Status.CHANGED -> transformJar(
                            inputJar,
                            outputJar,
                            function
                        )
                        Status.REMOVED -> FileUtils.delete(outputJar)
                    }
                } else {
                    transformJar(inputJar, outputJar, function)
                }
            }

            for (di in ti.directoryInputs) {
                val inputDir = di.file
                val outputDir = outputProvider.getContentLocation(
                    di.name,
                    di.contentTypes,
                    di.scopes,
                    Format.DIRECTORY
                )
                if (transformInvocation.isIncremental) {
                    for ((inputFile, value) in di.changedFiles) {
                        val out = toOutputFile(
                            outputDir,
                            inputDir,
                            inputFile
                        )
                        when (value ?: Status.NOTCHANGED) {
                            Status.NOTCHANGED -> {
                            }
                            Status.ADDED, Status.CHANGED -> if (!inputFile.isDirectory
                                && classFilter(inputFile.name)
                            ) {
                                transformFile(inputFile, out, function)
                            }
                            Status.REMOVED -> {
                                val outputFile = toOutputFile(
                                    outputDir,
                                    inputDir,
                                    inputFile
                                )
                                FileUtils.deleteIfExists(outputFile)
                            }
                        }
                    }
                } else {
                    for (`in` in FileUtils.getAllFiles(inputDir)) {
                        val out =
                            toOutputFile(outputDir, inputDir, `in`)
                        if (classFilter(`in`.name)) {
                            transformFile(`in`, out, function)
                        }
                    }
                }
            }
        }
        val msg = "transform $name 耗时: ${ (System.nanoTime() - start) / 1000000f } ms"
        BeautyLog.dog(msg)
        BeautyLog.log(name, false)
    }


    @Throws(IOException::class)
    open fun transformJar(
        inputJar: File,
        outputJar: File,
        function: BiConsumer<InputStream, OutputStream>?
    ) {
        Files.createParentDirs(outputJar)
        FileInputStream(inputJar).use { fis ->
            ZipInputStream(fis).use { zis ->
                FileOutputStream(outputJar).use { fos ->
                    ZipOutputStream(fos).use { zos ->
                        var entry = zis.nextEntry
                        while (entry != null && isValidZipEntryName(entry)) {
                            if (!entry.isDirectory && classFilter(entry.name)) {
                                zos.putNextEntry(ZipEntry(entry.name))
                                apply(function, zis, zos)
                            } else { // Do not copy resources
                            }
                            entry = zis.nextEntry
                        }
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    open fun transformFile(
        inputFile: File, outputFile: File, function: BiConsumer<InputStream, OutputStream>?
    ) {
        Files.createParentDirs(outputFile)
        FileInputStream(inputFile).use { fis ->
            FileOutputStream(outputFile).use { fos -> apply(function, fis, fos) }
        }
    }

    private fun toOutputFile(
        outputDir: File, inputDir: File, inputFile: File
    ): File {
        return File(
            outputDir,
            FileUtils.relativePossiblyNonExistingPath(inputFile, inputDir)
        )
    }

    @Throws(IOException::class)
    private fun apply(
        function: BiConsumer<InputStream, OutputStream>?,
        `in`: InputStream, out: OutputStream
    ) {
        try {
            function?.accept(`in`, out)
        } catch (e: UncheckedIOException) {
            throw e.cause!!
        }
    }
}