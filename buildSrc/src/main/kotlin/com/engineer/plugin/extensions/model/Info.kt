package com.engineer.plugin.extensions.model

/**
 * @author rookie
 * @since 12-04-2019
 */

data class Info(
    var path: String = "",
    var size: Long = 0,
    var time: String = "",
    var commitBranch: String = "",
    var commitId: String = ""
)