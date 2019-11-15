package com.engineer.plugin.actions

import com.engineer.plugin.actions.adapter.BuildSimpleListener
import groovy.json.JsonSlurper
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.logging.Logger


class CalculateAction {

    static Logger logger
    def static need_do_calc = false

    static apply(Project project) {
        logger = project.getLogger()
        project.gradle.getTaskGraph().afterTask { Task task ->
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

    static calculate(Project project) {
        printTag()
        project.android.applicationVariants.all { variant ->
            def dir = variant.getPackageApplication().outputDirectory
            def filePath = getApkFullPath(dir, variant.name)
            if (filePath != null) {
                def file = new File(filePath)
                if (file.exists()) {
                    def info = String.format("生成文件 %-53s 大小为 %.2f MB", file.name, formatValue(file.size()))
                    logger.error(info)
                } else {
                    logger.error("未生成任何 apk")
                }
            }
        }
        printTag()
    }

    static formatValue(value) {
        def ONE_KB = 1024.0f
        def ONE_MB = ONE_KB * ONE_KB
        def result = value / ONE_MB
        return result
    }

    static getApkFullPath(dir, name) {
        def output_json = new File(dir, "output.json")
        if (output_json.exists()) {
            Object object = new JsonSlurper().parse(output_json)
            def outputFile = dir.toString() + File.separator + object[0].path
            return outputFile
        } else {
            def error = String.format("%-14s not build", name)
            logger.error(error)
        }
    }

    static printTag() {
        println()
        logger.error("===================================生成 APK =================================")
        println()
    }
}