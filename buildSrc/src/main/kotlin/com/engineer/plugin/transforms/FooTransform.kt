package com.engineer.plugin.transforms

import org.apache.commons.io.IOUtils
import org.gradle.api.Project
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
//            simpleCopy(t,u)
        }
    }

    private fun simpleCopy(input: InputStream, output: OutputStream) {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(1024)
        while (-1 != input.read(buffer).also { n = it }) {
            output.write(buffer, 0, n)
            count += n.toLong()
        }
    }

    override fun getName(): String {
        return "foo"
    }

    override fun isEnabled(): Boolean {
        return true
    }
}