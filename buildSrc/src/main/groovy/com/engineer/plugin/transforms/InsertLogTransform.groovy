package com.engineer.plugin.transforms

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.io.ByteStreams
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

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

            String jarName = jar.name

            if (jarName.contains(TARGET_MODULE_NAME)) {
                def path = src.absolutePath
                println("find glide " + path)
                hack(src, dest)
            } else {
                FileUtils.copyFile(src, dest)
            }
        }
    }


    private static final String TARGET_MODULE_NAME = "com.github.bumptech.glide:glide"
    private static final String target = "com/bumptech/glide/RequestManager.class"

    private static hack(File sourceFile, File destFile) {

        String temp = sourceFile.absolutePath + ".temp"
        File tempFile = new File(temp)
        if (tempFile.exists()) {
            tempFile.delete()
        }

        JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(tempFile))

        JarFile jarFile = new JarFile(sourceFile)
        Enumeration<JarEntry> entries = jarFile.entries()
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement()
            String name = jarEntry.name

            InputStream inputStream = jarFile.getInputStream(jarEntry)
            ZipEntry zipEntry = new ZipEntry(name)
            outputStream.putNextEntry(zipEntry)

            if (name == target) {
                // todo hack
                ClassReader classReader = new ClassReader(inputStream)
                ClassWriter classWriter = new ClassWriter(classReader, 0)
                ClassVisitor classVisitor = new MyVisitor(Opcodes.ASM6, classWriter)
                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                byte[] bytes = classWriter.toByteArray()
                if (bytes != null) {
                    outputStream.write(bytes)
                } else {
                    println("sorry bytes is null")
                    ByteStreams.copy(inputStream, outputStream)
                }

            } else {
                ByteStreams.copy(inputStream, outputStream)
            }
            inputStream.close()
            outputStream.closeEntry()
        }
        outputStream.close()
        jarFile.close()

        if (sourceFile.exists()) {
            sourceFile.delete()
        }
        tempFile.renameTo(sourceFile)
        FileUtils.copyFile(sourceFile, destFile)
    }

    static class MyVisitor extends ClassVisitor {

        MyVisitor(int api, ClassVisitor cv) {
            super(api, cv)
        }

        void visit(int version, int access, String name, String signature,
                   String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces)
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            Type type = Type.getType(desc)


            println("acce==$access")
            println("name==$name")
            println("desc==$desc")
            println("type==$type")
            if (type != null && type.elementType != null ) {
                println("intr=${type.elementType.hasProperty("internalName")}")

            }else {
                println("null ")
            }
            println("sign==$signature")


            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)

            // && type.elementType.internalName == "java/lang/String"
            if (name == "load") {
                mv = new HackMethodVisitor(Opcodes.ASM6, mv)
            }
            return mv
        }
    }

    static class HackMethodVisitor extends MethodVisitor {

        HackMethodVisitor(int api, MethodVisitor mv) {
            super(api, mv)
        }

        @Override
        void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
//            println("TestMethodVisitor, owner = " + owner + ", name = " + name);
            //方法执行之前打印
            mv.visitLdcInsn(" zyq")
            mv.visitLdcInsn(" [ASM 测试] method in " + owner + " ,name=" + name)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false)
            mv.visitInsn(Opcodes.POP)

            super.visitMethodInsn(opcode, owner, name, desc, itf)
        }
    }

}