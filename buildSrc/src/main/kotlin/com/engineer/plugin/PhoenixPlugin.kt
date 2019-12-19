package com.engineer.plugin

import com.android.build.gradle.AppExtension
import com.engineer.plugin.actions.CalculateAction
import com.engineer.plugin.actions.RenameAction
import com.engineer.plugin.actions.TaskTimeAction
import com.engineer.plugin.extensions.PhoenixExtension
import com.engineer.plugin.transforms.FooTransform
import com.engineer.plugin.transforms.times.CatTransform
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
        }


//
        val appExtension = project.extensions.getByName("android")
        if (appExtension is AppExtension) {
            appExtension.registerTransform(CatTransform())

            val fooTransform = FooTransform()
            if (fooTransform.isEnabled()) {
                appExtension.registerTransform(fooTransform)
            }
        }
    }

}