package com.engineer.plugin.transforms.track


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
abstract class TrackClassVisitorFactory :
    AsmClassVisitorFactory<TrackClassVisitorFactory.TrackParam> {
    override fun isInstrumentable(classData: ClassData): Boolean {
        return true
    }

    interface TrackParam : InstrumentationParameters {
        @get:Input
        val trackOn: Property<Boolean>
    }

    override fun createClassVisitor(
        classContext: ClassContext, nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        val trackOn = parameters.orNull?.trackOn?.get() ?: false
        val api = instrumentationContext.apiVersion.get()
        return TrackClassVisitor(api, trackOn, nextClassVisitor)
    }
}