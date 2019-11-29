package com.engineer.plugin.actions

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.engineer.plugin.extensions.PhoenixExtension
import com.engineer.plugin.utils.Common
import org.gradle.api.Project

/**
 * @author rookie
 * @since 11-29-2019
 */
class RenameAction {
    companion object {
        fun apply(project: Project) {
            doRenameAction(project)
        }

        private fun doRenameAction(project: Project) {
            val phoenix = project.extensions.getByName("phoenix")
            if (phoenix is PhoenixExtension) {
                val rename = phoenix.rename


                val result = rename.result

                val prefix = rename.prefix
                val withTime = rename.withTime
                val withVariantName = rename.withVariantName
                val withVariantVersion = rename.withVariantVersion
                val onlyDebug = rename.onlyDebug

                val releaseTime = Common.releaseTime()

                val android = project.extensions.getByName("android")


                if (android is BaseAppModuleExtension) {
                    android.applicationVariants.all { variant ->
                        variant.outputs.all {
                            if (onlyDebug) {
                                if (variant.name.contains("Debug")) {
                                    val outputFileName = assembleName(
                                        prefix, releaseTime,
                                        withTime, withVariantName, withVariantVersion, variant
                                    )
                                    if (it is ApkVariantOutput) {
                                        it.outputFileName = outputFileName
                                    }
                                    result.invoke(outputFileName)
                                }
                            } else {
                                val outputFileName = assembleName(
                                    prefix, releaseTime,
                                    withTime, withVariantName, withVariantVersion, variant
                                )

                                if (it is ApkVariantOutput) {
                                    it.outputFileName = outputFileName
                                }

                                result.invoke(outputFileName)
                            }
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