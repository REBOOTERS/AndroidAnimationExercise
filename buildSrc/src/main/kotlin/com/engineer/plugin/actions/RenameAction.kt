package com.engineer.plugin.actions

import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.engineer.plugin.extensions.PhoenixExtension
import org.gradle.api.Project
import java.util.*

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 11-29-2019
 */
class RenameAction {
    companion object {
        fun apply(project: Project) {
            doRenameAction(project)
        }

        private fun doRenameAction(project: Project) {
            val phoenix = project.extensions.getByName("phoenix")
            println("phoenix $phoenix")
            if (phoenix is PhoenixExtension) {
                val rename = phoenix.rename

                if (rename.result == null) {
                    project.logger.info("result not set,will be ignored")
                }


                val result = rename.result

                val prefix = rename.prefix
                val withTime = rename.withTime
                val withVariantName = rename.withVariantName
                val withVariantVersion = rename.withVariantVersion
                val onlyDebug = rename.onlyDebug

                val releaseTime =
                    Date().toString().format("yyyy_MM_dd_HH_mm", TimeZone.getTimeZone("GMT+08:00"))

                val android = project.extensions.getByName("android")

                println("xxxxxxxxxxxxxxxxxx======> $android")

                if (android is BaseAppModuleExtension) {

                    android.applicationVariants.all { variant ->
                        variant.outputs.all {
                            it.outputFile.name
                            if (onlyDebug) {
                                if (variant.name.contains("Debug")) {
                                    val outputFileName = assembleName(
                                        prefix, releaseTime,
                                        withTime, withVariantName, withVariantVersion, variant
                                    )
                                    result.invoke(outputFileName)
                                }
                            } else {
                                val outputFileName = assembleName(
                                    prefix, releaseTime,
                                    withTime, withVariantName, withVariantVersion, variant
                                )
                                result.invoke(outputFileName)
                            }
//                    variant.getPackageApplication().outputDirectory = new File(destDir, variant.name)
                        }
                    }
                }
            }
        }

        private fun assembleName(
            prefix: String,
            releaseTime: String,
            withTime: Boolean,
            withVariantName: Boolean,
            withVariantVersion: Boolean,
            variant: ApplicationVariant
        ): String {
            val output = StringBuilder()
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
}