package com.engineer.plugin.actions

import groovy.json.JsonSlurper
import org.gradle.api.Project
import org.gradle.api.logging.Logger


class CalculateAction {

    static Logger logger

    static test(Project project) {
        println("test action")
        def tasks = project.tasks
        logger = project.getLogger()


        def need_do_calc = false
        tasks.forEach { task ->
            if (task.name.startsWith("assemble") && !task.name.contains("Test")) {
                logger.info("task :" + task.name)
                logger.info("task :" + task.state.getExecuted())
                if (task.state.executed) {
                    need_do_calc = true
                }
            }
        }
        logger.info("need=" + need_do_calc)

        if (need_do_calc) {
            calculate(project)
        } else {
            logger.info("no need to calc")
        }
    }

    static calculate(Project project) {
        logger.error("start calculate")
        project.android.applicationVariants.all { variant ->
            def dir = variant.getPackageApplication().outputDirectory
            def filePath = getApkFullPath(dir, variant.name)
            if (filePath != null) {
                def file = new File(filePath)
                def info = String.format("生成文件 %-53s 大小为 %.2f MB", file.name, formatValue(file.size()))
                logger.error(info)
            }
        }
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
}