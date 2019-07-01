package com.engineer.plugin.extensions

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

class PhoenixExtension {

    RenameExtension rename

    PhoenixExtension(ObjectFactory objectFactory) {
        rename = objectFactory.newInstance(RenameExtension)
    }

    void rename(Action<RenameExtension> action) {
        action.execute(rename)
    }
}