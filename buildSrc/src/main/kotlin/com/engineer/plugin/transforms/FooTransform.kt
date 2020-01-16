package com.engineer.plugin.transforms

import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.InputStream
import java.io.OutputStream
import java.util.function.BiConsumer

/**
 * @author rookie
 * @since 12-03-2019
 */
class FooTransform(project: Project) : BaseTransform(project) {

    override fun provideFunction(): BiConsumer<InputStream, OutputStream>? {
        return BiConsumer { t, u ->
            // just for test , do nothing
            IOUtils.copy(t, u)
        }
    }

    override fun getName(): String {
        return "foo"
    }

    override fun isEnabled(): Boolean {
        return true
    }
}