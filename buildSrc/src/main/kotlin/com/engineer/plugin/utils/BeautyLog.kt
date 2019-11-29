package com.engineer.plugin.utils

import org.gradle.api.logging.Logger

/**
 * @author rookie
 * @since 11-19-2019
 */
object BeautyLog {
    fun log(
        tag: String,
        isStart: Boolean,
        logger: Logger
    ) {
        if (isStart) {
            println()
            val msg =
                "=================================== $tag ===================================>>>>"
            logger.error(msg)
            println()
        } else {
            println()
            val msg =
                "<<<<=================================== $tag ==================================="
            logger.error(msg)
            println()
        }
    }
}