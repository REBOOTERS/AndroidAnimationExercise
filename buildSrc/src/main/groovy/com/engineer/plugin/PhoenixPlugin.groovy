package com.engineer.plugin

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

            doRenameAction(target)


            println()
            println "===================================PhoenixPlugin===============end=================="
            println()

        }

    }

    private void doRenameAction(Project project) {
        def rename = project.phoenix.rename
        if (rename.result == null ) {
            target.logger.info('result not set,will be ignored')
        }

        Closure result = rename.result

        String prefix = rename.prefix
        Boolean withTime = rename.withTime
        Boolean withVariantName = rename.withVariantName
        Boolean withVariantVersion = rename.withVariantVersion
        Boolean onlyDebug = rename.onlyDebug

        def releaseTime = new Date().format("yyyy_MM_dd_HH_mm", TimeZone.getTimeZone("GMT+08:00"))

        project.android.applicationVariants.all { variant ->
            variant.outputs.all {

                if (onlyDebug) {
                    if (variant.name.contains("Debug")) {
                        outputFileName= assembleName(prefix,releaseTime,
                                withTime,withVariantName,withVariantVersion,variant)
                        result(outputFileName)
                    }
                }else {
                    outputFileName= assembleName(prefix,releaseTime,
                            withTime,withVariantName,withVariantVersion,variant)
                    result(outputFileName)
                }
//                    variant.getPackageApplication().outputDirectory = new File(destDir, variant.name)
            }
        }
    }

    private static String assembleName(prefix, releaseTime, withTime, withVariantName, withVariantVersion, variant) {
        def output = new StringBuilder()
        output.append(prefix).append("_")

        if (withVariantVersion) {
            output.append( variant.versionName ).append("_")
        }
        if (withTime) {
            output.append(releaseTime).append("_")
        }
        if (withVariantName) {
            output.append(variant.name)
        }

        return output.append(".apk").toString()
    }


}