package com.engineer.plugin.actions

import com.engineer.plugin.actions.adapter.BuildSimpleListener
import com.engineer.plugin.extensions.model.TaskDetail
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.TaskState


class TaskTimeAction {
    //用来记录 task 的执行时长等信息
    static Map<String, TaskDetail> taskTimeMap = new HashMap<>()
    static Logger logger

    static apply(Project project) {
        logger = project.getLogger()

        project.gradle.addListener(new TaskExecutionListener() {
            @Override
            void beforeExecute(Task task) {
                TaskDetail detail = new TaskDetail()
                detail.start = System.currentTimeMillis()
                detail.name = task.path
                println("task name " + task.path)
                taskTimeMap.put(task.path, detail)
            }

            @Override
            void afterExecute(Task task, TaskState taskState) {
                TaskDetail detail = taskTimeMap.get(task.getPath())
                detail.end = System.currentTimeMillis()
                detail.total = detail.end - detail.start
            }
        })
//
        project.gradle.addBuildListener(new BuildSimpleListener() {
            @Override
            void buildFinished(BuildResult buildResult) {
                super.buildFinished(buildResult)
                if (taskTimeMap.isEmpty()) {
                    return
                }
                printTag()

                taskTimeMap.sort { a, b ->
                    b.value.total - a.value.total
                }.keySet().forEach { name ->
                    def cost_time = taskTimeMap.get(name).total

                    def info = String.format("task %-70s spend %d ms", name, cost_time)
                    project.logger.log(LogLevel.ERROR, info)
                }

                printTag()
            }
        })
    }

    static printTag() {
        println()
        logger.error("=================================== Task 耗时 ===============================================")
        println()
    }
}