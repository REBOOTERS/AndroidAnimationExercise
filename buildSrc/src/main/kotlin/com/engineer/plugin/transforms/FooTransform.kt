package com.engineer.plugin.transforms

import com.android.utils.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.internal.impldep.org.apache.commons.lang.reflect.FieldUtils
import shadow.bundletool.com.android.tools.r8.utils.StreamUtils
import sun.reflect.misc.FieldUtil
import java.io.InputStream
import java.io.OutputStream
import java.util.function.BiConsumer

/**
 * @author rookie
 * @since 12-03-2019
 */
class FooTransform : BaseTransform() {

    override fun provideFunction(): BiConsumer<InputStream, OutputStream>? {
        return BiConsumer { t, u ->
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