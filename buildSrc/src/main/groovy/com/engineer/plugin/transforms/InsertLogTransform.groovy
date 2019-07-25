package com.engineer.plugin.transforms

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.util.jar.JarFile

class InsertLogTransform extends Transform {

    private Project project

    InsertLogTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return InsertLogTransform.class.simpleName
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    private static printlnInvocationInfo(TransformInvocation transformInvocation) {
        println("context==" + transformInvocation.context)
        println("incremental==" + transformInvocation.incremental)
        println("inputs.jarInputs==" + transformInvocation.inputs["jarInputs"])
        println("inputs.directoryInputs==" + transformInvocation.inputs["directoryInputs"])
        println("referencedInputs==" + transformInvocation.referencedInputs)
        println("outputProvider==" + transformInvocation.outputProvider)
    }


    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
//        printlnInvocationInfo(transformInvocation)

        def jars = transformInvocation.inputs["jarInputs"].get(0)
        println("jars size ==" + jars.size())
        jars.forEach { jar ->
//            println()
//            println("jar  :" + jar)
//            println("jar  :" + jar.file)
//            println("jar  :" + jar.file.absolutePath)
//            println()

            File src = jar.file
            def dest = transformInvocation.outputProvider.getContentLocation(
                    jar.name,
                    jar.contentTypes,
                    jar.scopes,
                    Format.JAR
            )

            hack(jar.name, src, dest)

            FileUtils.copyFile(src, dest)
        }
    }


    private static final String TARGET_MODULE_NAME = "com.github.bumptech.glide:glide"

    private static hack(String name, File sourceFile, File destFile) {
        def path = sourceFile.absolutePath
        if (name.contains(TARGET_MODULE_NAME)) {
            println("name ==" + name)
            println("find glide " + path)

            JarFile file = new JarFile(sourceFile)
        }

    }


}