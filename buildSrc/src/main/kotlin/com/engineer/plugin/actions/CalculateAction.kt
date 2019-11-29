package com.engineer.plugin.actions

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.engineer.plugin.extensions.model.ApkOutputInfo
import com.engineer.plugin.utils.BeautyLog
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import groovy.json.JsonSlurper
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 11-29-2019
 */
class CalculateAction {

    companion object {

        private lateinit var logger: Logger
        var need_do_calc = false

        fun apply(project: Project) {
            logger = project.getLogger()
            project.gradle.getTaskGraph().afterTask { task ->
                println("task name " + task.name)

                if (task.name.startsWith("assemble") && !task.name.contains("Test")) {

                    if (task.state.executed) {
                        need_do_calc = true
                    }
                    logger.info("need=" + need_do_calc)

                    if (need_do_calc) {
                        calculate(project)
                    } else {
                        logger.info("no need to calc")
                    }
                }
            }
        }

        private fun calculate(project: Project) {
            printTag(true)
            var hasFile = false


            val android = project.extensions.getByName("android")

            if (android is BaseAppModuleExtension) {
                android.applicationVariants.all { variant ->
                    variant.packageApplicationProvider.configure {
                        val dir = it.outputDirectory
                        val filePath = getApkFullPath(dir.absolutePath, variant.name)
                        val file = File(filePath)
                        if (file.exists()) {
                            hasFile = true
                            val info = String.format(
                                "生成文件 %-53s 大小为 %.2f MB",
                                file.name,
                                formatValue(file.length())
                            )
                            logger.error(info)
                        } else {
//                        logger.error("未生成任何 apk")
                        }
                    }
                }
            }


            if (hasFile) {
                logger.error("time   " + releaseTime())
                logger.error("branch " + getGitBranch(project))
                logger.error("commit " + getGitCommit(project))
            }

            printTag(false)
        }

        private fun formatValue(value: Long): Float {
            val ONE_KB = 1024.0f
            val ONE_MB = ONE_KB * ONE_KB
            val result = value / ONE_MB
            return result
        }

        fun getApkFullPath(dir: String, name: String): String {
            val output_json = File(dir, "output.json")
            if (output_json.exists()) {
                val stream = Files.newInputStream(Paths.get(output_json.absolutePath))
                stream.buffered().reader().use {
                    val json = it.readText()
                    println("the json === $json")
                    val type = object : TypeToken<List<ApkOutputInfo>>() {}.type
                    val result =
                        Gson().fromJson<List<ApkOutputInfo>>(json, type)
                    val outputFile = dir + File.separator + result[0].path
                    return outputFile
                }
            } else {
                val error = String.format("%-14s not build", name)
                logger.error(error)
                return ""
            }
        }

        fun printTag(start: Boolean) {
            BeautyLog.log("生成 APK", start, logger)
        }

        fun releaseTime(): String {
            return Date().toString().format("yyyy-MM-dd HH:mm", TimeZone.getTimeZone("GMT+08:00"))
        }

        fun getGitCommit(project: Project): String {
            val stdout = ByteArrayOutputStream()
            project.exec {
                it.commandLine("git")
                it.args("rev-parse", "--short", "HEAD")
                it.standardOutput = stdout
            }
            return stdout.toString().trim()
        }

        fun getGitBranch(project: Project): String {
            val stdout = ByteArrayOutputStream()
            project.exec {
                it.commandLine("git")
                it.args("rev-parse", "--abbrev-ref", "HEAD")
                it.standardOutput = stdout
            }
            return stdout.toString().trim()
        }

    }
}