package com.engineer.plugin

import com.engineer.plugin.actions.RenameAction
import com.engineer.plugin.extensions.PhoenixExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class PhoenixPlugin implements Plugin<Project> {


    @Override
    void apply(Project target) {

        target.extensions.create('phoenix', PhoenixExtension.class,target.objects)


        target.afterEvaluate {

            println()
            println "===================================PhoenixPlugin===============begin=================="
            println()

            if (!target.android) {
                throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!')
            }

            RenameAction.doRenameAction(target)

            println()
            println "===================================PhoenixPlugin===============end=================="
            println()

        }

    }

}