package com.engineer.plugin.extensions

/**
 * @author rookie
 * @since 11-29-2019
 */
open class RenameExtension {

    var result: (aa: String) -> Unit? = {}
    var prefix = "animation"
    var withTime = false
    var withVariantName = false
    var withVariantVersion = false
    var onlyDebug = false
}