package com.engineer.plugin.actions

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.engineer.plugin.extensions.model.ApkOutputInfo
import com.engineer.plugin.extensions.model.Info
import com.engineer.plugin.utils.Common
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import groovy.json.JsonOutput
import org.gradle.api.Project
import org.gradle.api.tasks.TaskState
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author rookie
 * @since 11-29-2019
 */
class CalculateAction(project: Project) : BaseAction(project) {

    private var needDoCalc = false

    override fun apply() {
        project.gradle.taskGraph.afterTask { task ->
            println("task name " + task.path)

            if (task.name.startsWith("assemble") && !task.name.contains("Test")) {

                if (task.state.executed) {
                    needDoCalc = true
                }
                logger.info("need=$needDoCalc")

                if (needDoCalc) {
                    calculate(project)
                } else {
                    logger.info("no need to calc")
                }
            }
        }
    }

    private fun calculate(project: Project) {
        printTag(true, "生成 APK")


        val android = project.extensions.getByName("android")
        if (android is BaseAppModuleExtension) {
            android.applicationVariants.all { variant ->
                variant.packageApplicationProvider.configure {
                    val dir = it.outputDirectory
                    val filePath = getApkFullPath(dir.absolutePath, variant.name)
                    val file = File(filePath)
                    if (file.exists()) {
                        val info = String.format(
                            "生成文件 %-53s 大小为 %.2f MB",
                            file.name, formatValue(file.length())
                        )
                        logger.error(info)

                        genInfos(filePath)
                    } else {
//                        logger.error("未生成任何 apk")
                    }
                }
            }
        }

        printTag(false, "生成 APK")
    }

    private fun genInfos(filePath: String) {
        val time = Common.releaseTime()
        val branch = Common.getGitBranch(project)
        val commitId = Common.getGitCommit(project)
        val path = filePath
        val size = File(filePath).length()

        logger.error(String.format("%-13s : %s ", "time", time))
        logger.error(String.format("%-13s : %s ", "branch", branch))
        logger.error(String.format("%-13s : %s ", "commitId", commitId))
        logger.error(String.format("%-13s : %s ", "path", path))
        logger.error(String.format("%-13s : %s ", "size", size))


        val infos: ArrayList<Info> = ArrayList()
        val outFile = project.rootProject.file("./gradle/infos.json")
        if (outFile.exists()) {
            val stream = Files.newInputStream(Paths.get(outFile.absolutePath))
            stream.buffered().reader().use {
                val json = it.readText()
                val type = object : TypeToken<List<Info>>() {}.type
                infos.addAll(Gson().fromJson<List<Info>>(json, type))
            }
        }

        val info = Info()
        info.time = time
        info.commitBranch = branch
        info.commitId = commitId
        info.path = path
        info.size = size

        infos.add(info)
        val pretty = JsonOutput.toJson(infos)
//        println("output=====>$pretty")
        val json = JsonOutput.prettyPrint(pretty)
        outFile.writeText(json)
    }

    private fun formatValue(value: Long): Float {
        val oneKB = 1024.0f
        val oneMB = oneKB * oneKB
        return value / oneMB
    }

    private fun getApkFullPath(dir: String, name: String): String {
        val outputJson = File(dir, "output.json")
        if (outputJson.exists()) {
            val stream = Files.newInputStream(Paths.get(outputJson.absolutePath))
            stream.buffered().reader().use {
                val json = it.readText()
                val type = object : TypeToken<List<ApkOutputInfo>>() {}.type
                val result =
                    Gson().fromJson<List<ApkOutputInfo>>(json, type)
                return dir + File.separator + result[0].path
            }
        } else {
            val error = String.format("%-14s not build", name)
            logger.error(error)
            return ""
        }
    }

}