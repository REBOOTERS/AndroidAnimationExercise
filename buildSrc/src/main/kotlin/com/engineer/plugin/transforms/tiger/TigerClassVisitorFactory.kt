package com.engineer.plugin.transforms.tiger


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
abstract class TigerClassVisitorFactory :
    AsmClassVisitorFactory<TigerClassVisitorFactory.TrackParam> {
    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

    interface TrackParam : InstrumentationParameters {
        @get:Input
        val tigerOn: Property<Boolean>
    }

    override fun createClassVisitor(
        classContext: ClassContext, nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val tigerOn = parameters.orNull?.tigerOn?.get() ?: false
        val api = instrumentationContext.apiVersion.get()
        return TigerClassVisitor(api, tigerOn, nextClassVisitor)
    }
}