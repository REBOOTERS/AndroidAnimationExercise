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
import org.objectweb.asm.commons.AdviceAdapter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class GlideLoadLogTransform extends Transform {

    private Project project

    GlideLoadLogTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return GlideLoadLogTransform.class.simpleName
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

        transformInvocation.inputs.forEach{ input ->
            input.jarInputs.forEach { jar ->
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
//                FileUtils.copyFile(src, dest)
                } else {
                    FileUtils.copyFile(src, dest)
                }
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
            Type[] types = Type.getArgumentTypes(desc)


            println()
            println("<========================")
            println("acce==$access")
            println("name==$name")
            println("desc==$desc")
            println("sign==$signature")
            if (types != null) {
                for (int i = 0; i < types.length; i++) {
                    Type type = types[i]
                    println("type.desc  " + type.descriptor)
                    println("type.inter " + type.internalName)
//                    println("type "+type.argumentsAndReturnSizes)
                    println("type.class " + type.className)
                    if (type.className.equals("java.lang.String")) {
                        println("bingo")
                    }
                }
            }

            println("========================>")
            println()


            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)

            // && type.elementType.internalName == "java/lang/String"
            if (name == "load" && descMatch(desc)) {
                println("find load")

//                mv = new HackMethodVisitor(Opcodes.ASM6, mv)
                mv = new HackMethodAdapter(Opcodes.ASM6, mv, access, name, desc)
            }
            return mv
        }
    }

    static boolean descMatch(String desc) {
        Type[] types = Type.getArgumentTypes(desc)
        if (types != null) {
            for (int i = 0; i < types.length; i++) {
                Type type = types[i]
                String className = type.className
                if (className.equals("java.lang.String")) {
                    return true;
                }
            }
        }
        return false
    }

    static class HackMethodAdapter extends AdviceAdapter {

        /**
         * Creates a new {@link AdviceAdapter}.
         *
         * @param api
         *            the ASM API version implemented by this visitor. Must be one
         *            of {@link Opcodes#ASM4}, {@link Opcodes#ASM5} or {@link Opcodes#ASM6}.
         * @param mv
         *            the method visitor to which this adapter delegates calls.
         * @param access
         *            the method's access flags (see {@link Opcodes}).
         * @param name
         *            the method's name.
         * @param desc
         *            the method's descriptor (see {@link Type Type}).
         */
        protected HackMethodAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc)
        }

        @Override
        protected void onMethodEnter() {
            super.onMethodEnter()
            println("insert before return")
            mv.visitLdcInsn("hack")
            mv.visitVarInsn(Opcodes.ALOAD, 1)
//            mv.visitLdcInsn(" [ASM 测试] method in " + owner + " ,name=" + name)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false)
        }
    }

}