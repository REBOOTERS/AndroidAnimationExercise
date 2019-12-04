package com.engineer.plugin.actions

import com.engineer.plugin.actions.adapter.BuildSimpleListener
import com.engineer.plugin.extensions.model.TaskDetail
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskState
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.Comparator
import kotlin.collections.HashMap

/**
 * @author rookie
 * @since 11-29-2019
 */
class TaskTimeAction(project: Project) : BaseAction(project) {
    //用来记录 task 的执行时长等信息
    val taskTimeMap = HashMap<String, TaskDetail>()

    override fun apply() {

        project.gradle.addListener(object : TaskExecutionListener {

            override fun beforeExecute(task: Task) {
                val detail = TaskDetail()
                detail.start = System.currentTimeMillis()
                detail.name = task.path
                println("task name " + task.path)
                taskTimeMap[task.path] = detail
            }

            override fun afterExecute(task: Task, taskState: TaskState) {
                val detail = taskTimeMap[task.path]!!
                detail.end = System.currentTimeMillis()
                detail.total = detail.end - detail.start
            }
        })

        project.gradle.addBuildListener(object : BuildSimpleListener() {

            override fun buildFinished(buildResult: BuildResult) {
                super.buildFinished(buildResult)
                if (taskTimeMap.isEmpty()) {
                    return
                }
                printTag(true, "Task 耗时")

                val list = ArrayList<Map.Entry<String, TaskDetail>>(taskTimeMap.entries)
                list.sortWith(Comparator { p0, p1 -> p1!!.value.compareTo(p0!!.value) })
                list.forEach {
                    val costTime = it.value.total
                    val info =
                        String.format("task %-70s spend %d ms", it.value.name, costTime)
                    logger.log(LogLevel.ERROR, info)
                }
                printTag(false, "Task 耗时")
            }

        })
    }

    fun formatTime(time: Long): Long {
        return TimeUnit.NANOSECONDS.toMillis(time)
    }
}