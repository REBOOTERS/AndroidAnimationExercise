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
            val msg =
                "\n=========================================== $tag ========================================>>>>\n"
            logger.error(msg)
        } else {
            val msg =
                "\n<<<<======================================= $tag ===========================================\n"
            logger.error(msg)
        }
    }

    fun dog(info: String, logger: Logger) {
        logger.error("\n===================================================================================>>>>\n")
        logger.error(info)
        logger.error("\n===================================================================================>>>>\n")
    }
}