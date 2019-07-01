package com.engineer.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class ApkRenamePlugin implements Plugin<Project> {


    @Override
    void apply(Project target) {

        target.extensions.create(config_alias, ApkDistExtension)


        target.afterEvaluate {

            println()
            println "===================================ApkRenamePlugin===============begin=================="
            println()

            if (!target.android) {
                throw new IllegalStateException('Must apply \'com.android.application\' or \'com.android.library\' first!')
            }

            if (target.apkconfig.nameMap == null || target.apkconfig.destDir == null) {
                target.logger.info('Apk dist conf should be set!')
                return
            }

            Closure nameMap = target[config_alias].nameMap
            String destDir = target[config_alias].destDir

            def releaseTime = new Date().format("yyyy:MM:dd:HH:mm", TimeZone.getTimeZone("GMT+08:00"))

            target.android.applicationVariants.all { variant ->
                variant.outputs.all {

                    outputFileName = "animation_${variant.versionName}_${releaseTime}_${variant.name}.apk"
                    nameMap(outputFileName)
//                    variant.getPackageApplication().outputDirectory = new File(destDir, variant.name)
                }
            }
            println()
            println "===================================ApkRenamePlugin===============end=================="
            println()

        }


    }


    def config_alias = 'apkconfig'
}