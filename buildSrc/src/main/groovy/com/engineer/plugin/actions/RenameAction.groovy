package com.engineer.plugin.actions

import org.gradle.api.Project

class RenameAction {

    static void doRenameAction(Project project) {
        def rename = project.phoenix.rename
        if (rename.result == null) {
            project.logger.info('result not set,will be ignored')
        }

        Closure result = rename.result

        String prefix = rename.prefix
        boolean withTime = rename.withTime
        boolean withVariantName = rename.withVariantName
        boolean withVariantVersion = rename.withVariantVersion
        boolean onlyDebug = rename.onlyDebug

        def releaseTime = new Date().format("yyyy_MM_dd_HH_mm", TimeZone.getTimeZone("GMT+08:00"))

        project.android.applicationVariants.all { variant ->
            variant.outputs.all {

                if (onlyDebug) {
                    if (variant.name.contains("Debug")) {
                        outputFileName = assembleName(prefix, releaseTime,
                                withTime, withVariantName, withVariantVersion, variant)
                        result(outputFileName)
                    }
                } else {
                    outputFileName = assembleName(prefix, releaseTime,
                            withTime, withVariantName, withVariantVersion, variant)
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
            output.append(variant.versionName).append("_")
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