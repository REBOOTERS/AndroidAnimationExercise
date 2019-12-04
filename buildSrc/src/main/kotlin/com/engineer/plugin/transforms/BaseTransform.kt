package com.engineer.plugin.transforms

import com.android.SdkConstants
import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.builder.utils.isValidZipEntryName
import com.android.utils.FileUtils
import com.google.common.io.Files
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
abstract class BaseTransform : Transform() {

    abstract fun provideFunction(): BiConsumer<InputStream, OutputStream>?


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
                            function,
                            inputJar,
                            outputJar
                        )
                        Status.REMOVED -> FileUtils.delete(outputJar)
                    }
                } else {
                    transformJar(function, inputJar, outputJar)
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
                        when (value ?: Status.NOTCHANGED) {
                            Status.NOTCHANGED -> {
                            }
                            Status.ADDED, Status.CHANGED -> if (!inputFile.isDirectory
                                && inputFile.name
                                    .endsWith(SdkConstants.DOT_CLASS)
                            ) {
                                val out = toOutputFile(
                                    outputDir,
                                    inputDir,
                                    inputFile
                                )
                                transformFile(function, inputFile, out)
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
                        if (`in`.name.endsWith(SdkConstants.DOT_CLASS)) {
                            val out =
                                toOutputFile(outputDir, inputDir, `in`)
                            transformFile(function, `in`, out)
                        }
                    }
                }
            }
        }
    }


    @Throws(IOException::class)
    open fun transformJar(
        function: BiConsumer<InputStream, OutputStream>?,
        inputJar: File,
        outputJar: File
    ) {
        Files.createParentDirs(outputJar)
        FileInputStream(inputJar).use { fis ->
            ZipInputStream(fis).use { zis ->
                FileOutputStream(outputJar).use { fos ->
                    ZipOutputStream(fos).use { zos ->
                        var entry = zis.nextEntry
                        while (entry != null && isValidZipEntryName(entry)) {
                            if (!entry.isDirectory && entry.name.endsWith(SdkConstants.DOT_CLASS)) {
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
        function: BiConsumer<InputStream, OutputStream>?,
        inputFile: File,
        outputFile: File
    ) {
        Files.createParentDirs(outputFile)
        FileInputStream(inputFile).use { fis ->
            FileOutputStream(outputFile).use { fos -> apply(function, fis, fos) }
        }
    }

    open fun toOutputFile(
        outputDir: File,
        inputDir: File,
        inputFile: File
    ): File {
        return File(
            outputDir,
            FileUtils.relativePossiblyNonExistingPath(inputFile, inputDir)
        )
    }

    @Throws(IOException::class)
    open fun apply(
        function: BiConsumer<InputStream, OutputStream>?,
        `in`: InputStream,
        out: OutputStream
    ) {
        try {
            function?.accept(`in`, out)
        } catch (e: UncheckedIOException) {
            throw e.cause!!
        }
    }
}