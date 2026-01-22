package com.engineer.plugin

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.engineer.plugin.extensions.PhoenixExtension
import com.engineer.plugin.extensions.TransformExtension
import com.engineer.plugin.transforms.FooClassVisitorFactory
import com.engineer.plugin.transforms.cat.CatClassVisitorFactory
import com.engineer.plugin.transforms.tiger.TigerClassVisitorFactory
import com.engineer.plugin.transforms.track.TrackClassVisitorFactory
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author rookie
 * @since 11-29-2019
 */
abstract class PhoenixPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.create("phoenix", PhoenixExtension::class.java, project.objects)

        println()
        println("===================================PhoenixPlugin===============begin==================")
        println()


        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        val transformExtension = getTransformConfig(project)
        androidComponents.onVariants { variant ->
            variant.instrumentation.transformClassesWith(
                FooClassVisitorFactory::class.java, InstrumentationScope.ALL
            ) {}
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)

            variant.instrumentation.transformClassesWith(
                TrackClassVisitorFactory::class.java, InstrumentationScope.PROJECT
            ) {
                it.trackOn.set(transformExtension.trackOn)
            }
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_METHODS)

            variant.instrumentation.transformClassesWith(
                CatClassVisitorFactory::class.java, InstrumentationScope.ALL
            ) {
                it.catOn.set(transformExtension.catOn)
            }

            variant.instrumentation.transformClassesWith(
                TigerClassVisitorFactory::class.java, InstrumentationScope.ALL
            ) {
                it.tigerOn.set(transformExtension.tigerOn)
            }
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COMPUTE_FRAMES_FOR_INSTRUMENTED_CLASSES)
        }

        println()
        println("===================================PhoenixPlugin===============end==================")
        println()
    }

    private fun getTransformConfig(project: Project): TransformExtension {
        val phoenix = project.extensions.findByType(PhoenixExtension::class.java)
        if (phoenix == null) {
            val transformExtension = TransformExtension()
            transformExtension.catOn = false
            transformExtension.tigerOn = false
            transformExtension.trackOn = false
            transformExtension.tigerClassList = HashMap()
            return transformExtension
        }
        return phoenix.transform
    }

}