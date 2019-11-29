package com.engineer.plugin

import com.android.build.gradle.AppExtension
import com.engineer.plugin.actions.CalculateAction
import com.engineer.plugin.actions.RenameAction
import com.engineer.plugin.actions.TaskTimeAction
import com.engineer.plugin.extensions.PhoenixExtension
import com.engineer.plugin.transforms.times.CatTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author zhuyongging @ Zhihu Inc.
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

            println()
            println("===================================PhoenixPlugin===============end==================")
            println()

        }


        TaskTimeAction.apply(project)
        CalculateAction.apply(project)
//
        val appExtension = project.extensions.getByName("android")
        if (appExtension is AppExtension) {
//            appExtension.registerTransform(CatTransform())
//                appExtension.registerTransform(new GlideLoadLogTransform(project))
        }
    }

}