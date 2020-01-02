package com.engineer.plugin

import com.android.build.gradle.AppExtension
import com.engineer.plugin.actions.CalculateAction
import com.engineer.plugin.actions.RenameAction
import com.engineer.plugin.actions.TaskTimeAction
import com.engineer.plugin.extensions.PhoenixExtension
import com.engineer.plugin.transforms.FooTransform
import com.engineer.plugin.transforms.cost.CatTransform
import com.engineer.plugin.utils.GitTool
import com.engineer.plugin.utils.JsonTool
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author rookie
 * @since 11-29-2019
 */
class PhoenixPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create("phoenix", PhoenixExtension::class.java, project.objects)
        project.afterEvaluate {

            println()
            println("===================================PhoenixPlugin===============begin==================")
            println()


            RenameAction.apply(project)
            TaskTimeAction(project).apply()
            CalculateAction(project).apply()

            println()
            println("===================================PhoenixPlugin===============end==================")
            println()

            JsonTool.test()
            GitTool.getGitBranch(project)
        }

        registerTransform(project)
    }

    private fun registerTransform(project: Project) {
        val appExtension = project.extensions.getByName("android")
        if (appExtension is AppExtension) {

            val phoenix = project.extensions.getByName("phoenix")

            if (phoenix is PhoenixExtension) {

                println("now phoenix is :\n  $phoenix\n")

                val setting = phoenix.trasform

                if (setting.catTransformOn) {
                    appExtension.registerTransform(CatTransform())
                } else {
                    println("catTransform disabled")
                }


                val fooTransform = FooTransform()
                if (setting.fooTransformOn && fooTransform.isEnabled()) {
                    appExtension.registerTransform(fooTransform)
                } else {
                    println("FooTransform disabled")
                }
            }
        }
    }

}