package com.engineer.plugin.extensions

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

/**
 * @author rookie
 * @since 11-29-2019
 */

open class PhoenixExtension(objectFactory: ObjectFactory) {
    var rename: RenameExtension = objectFactory.newInstance(RenameExtension::class.java)
    var transform: TransformExtension = objectFactory.newInstance(TransformExtension::class.java)
    //

    fun rename(action: Action<RenameExtension>) {
        action.execute(rename)
    }

    fun transform(action: Action<TransformExtension>) {
        action.execute(transform)
    }

    override fun toString(): String {
        return "PhoenixExtension(rename=$rename, settings=$transform)"
    }


}