package com.engineer.plugin.actions

import com.engineer.plugin.utils.BeautyLog
import org.gradle.api.Project
import org.gradle.api.logging.Logger

/**
 * @author rookie
 * @since 12-04-2019
 */
abstract class BaseAction(p: Project) {
    var logger: Logger = p.logger
    var project: Project = p

    abstract fun apply()

    fun printTag(start: Boolean, tag: String) {
        BeautyLog.log(tag, start, logger)
    }
}