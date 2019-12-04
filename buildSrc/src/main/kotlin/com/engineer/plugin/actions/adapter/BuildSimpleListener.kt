package com.engineer.plugin.actions.adapter

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle

/**
 * @author rookie
 * @since 11-29-2019
 */

abstract class BuildSimpleListener : BuildListener {
    override fun settingsEvaluated(p0: Settings) {

    }

    override fun buildFinished(buildResult: BuildResult) {
    }

    override fun projectsLoaded(p0: Gradle) {
    }

    override fun buildStarted(p0: Gradle) {
    }

    override fun projectsEvaluated(p0: Gradle) {
    }

}