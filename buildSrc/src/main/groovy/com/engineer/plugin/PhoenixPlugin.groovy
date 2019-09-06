package com.engineer.plugin

import com.android.build.gradle.AppExtension
import com.engineer.plugin.actions.CalculateAction
import com.engineer.plugin.actions.RenameAction
import com.engineer.plugin.actions.TaskTimeAction
import com.engineer.plugin.extensions.PhoenixExtension
import com.engineer.plugin.transforms.GlideLoadLogTransform
import com.engineer.plugin.transforms.times.CatTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class PhoenixPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {

        project.extensions.create('phoenix', PhoenixExtension.class, project.objects)
        project.afterEvaluate {

            println()
            println "===================================PhoenixPlugin===============begin=================="
            println()

            if (!project.android) {
                throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!')
            }

            RenameAction.doRenameAction(project)

            println()
            println "===================================PhoenixPlugin===============end=================="
            println()

        }

        project.gradle.buildFinished {
            CalculateAction.test(project)
        }

        TaskTimeAction.apply(project)

        def appExtension = project.extensions.getByName("android")
        if (appExtension != null) {
            if (appExtension instanceof AppExtension) {
                appExtension.registerTransform(new CatTransform())
                appExtension.registerTransform(new GlideLoadLogTransform(project))
            }
        }
    }
}