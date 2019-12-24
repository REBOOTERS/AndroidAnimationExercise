package com.engineer.plugin.extensions

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 12-23-2019
 *
 *
 * 所有 Transform 的开关，这个是优先级好的
 */
open class TransformExtension {

    var catTransformOn = true
    var fooTransformOn = true

    override fun toString(): String {
        return "TransformExtension(catTransformOn=$catTransformOn, fooTransformOn=$fooTransformOn)"
    }

}