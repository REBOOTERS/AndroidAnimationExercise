package com.engineer.plugin.transforms.cat


import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationParameters
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.util.TraceClassVisitor
import java.io.PrintWriter

/**
 * @author rookie
 * @since 12-03-2019
 */
abstract class CatClassVisitorFactory : AsmClassVisitorFactory<CatClassVisitorFactory.TrackParam> {
    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

    interface TrackParam : InstrumentationParameters {
        @get:Input
        val catOn: Property<Boolean>
    }

    override fun createClassVisitor(
        classContext: ClassContext, nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val catOn = parameters.orNull?.catOn?.get() ?: false
        val api = instrumentationContext.apiVersion.get()
        return CatClassVisitor(api, catOn, nextClassVisitor)
    }
}