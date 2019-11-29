package com.engineer.plugin.actions

import com.engineer.plugin.actions.adapter.BuildSimpleListener
import com.engineer.plugin.extensions.model.TaskDetail
import com.engineer.plugin.utils.BeautyLog
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger

import org.gradle.api.tasks.TaskState
import java.text.SimpleDateFormat

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 11-29-2019
 */
class TaskTimeAction {
    companion object {
        //用来记录 task 的执行时长等信息
        val taskTimeMap = HashMap<String, TaskDetail>()
        private lateinit var logger: Logger

        fun apply(project: Project) {
            logger = project.logger

            project.gradle.addListener(object : TaskExecutionListener {

                override fun beforeExecute(task: Task) {
                    val detail = TaskDetail()
                    detail.start = System.currentTimeMillis()
                    detail.name = task.path
                    println("task name " + task.path)
                    taskTimeMap.put(task.path, detail)
                }

                override fun afterExecute(task: Task, taskState: TaskState) {
                    val detail = taskTimeMap[task.path]!!
                    detail.end = System.currentTimeMillis()
                    detail.total = detail.end - detail.start
                }
            })
//
            project.gradle.addBuildListener(object : BuildSimpleListener() {

                override fun buildFinished(buildResult: BuildResult) {
                    super.buildFinished(buildResult)
                    if (taskTimeMap.isEmpty()) {
                        return
                    }
                    printTag(true)



                    taskTimeMap
                        .toSortedMap()
                        .forEach {
                            val cost_time = it.value.total
                            val info =
                                String.format("task %-70s spend %d ms", it.value.name, cost_time)
                            logger.log(LogLevel.ERROR, info)
                        }
                    printTag(false)
                }

            })
        }

        fun printTag(start: Boolean) {
            BeautyLog.log("Task 耗时", start, logger)
        }

        fun formatTime(time: Float): String {
            println("time is " + time)
            val dateformat = SimpleDateFormat("mm:ss")
            val result = dateformat.format(time)
            return result
        }
    }
}