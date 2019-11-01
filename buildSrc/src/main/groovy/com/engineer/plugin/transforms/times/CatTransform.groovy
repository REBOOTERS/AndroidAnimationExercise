package com.engineer.plugin.transforms.times

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

/**
 * @author rookie
 * @since 09-03-2019
 */
class CatTransform extends Transform {

    @Override
    String getName() {
        return "cat"
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

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        transformInvocation.inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                if (directoryInput.file.isDirectory()) {
                    directoryInput.file.eachFileRecurse { File file ->
                        def name = file.name
                        if (name.endsWith(".class") && !(name == ("R.class"))
                                && !name.startsWith("R\$") && !(name == ("BuildConfig.class"))) {

                            ClassReader reader = new ClassReader(file.bytes)
                            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
                            ClassVisitor visitor = new CatClassVisitor(writer)
                            reader.accept(visitor, ClassReader.EXPAND_FRAMES)

                            byte[] code = writer.toByteArray()
                            def classPath = file.parentFile.absolutePath + File.separator + name
                            FileOutputStream fos = new FileOutputStream(classPath)
                            fos.write(code)
                            fos.close()
                        }
                    }
                }

                def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)


                FileUtils.copyDirectory(directoryInput.file, dest)
            }

            // todo 从 jar 文件里找到 class
            input.jarInputs.each { JarInput jarInput ->
                def src = jarInput.file
                def dest = transformInvocation.outputProvider.getContentLocation(jarInput.name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)

//                println("jar Name "+jarInput.name)
//                println("dest     "+dest.absolutePath)
//                println("jar path "+src.absolutePath)
//                println("jar name "+src.name)

                if (jarInput.name == "include") {

                    println("jar Name " + jarInput.name)
                    println("dest     " + dest.absolutePath)
                    println("jar path " + src.absolutePath)
                    println("jar name " + src.name)

                    String temp = src.absolutePath.substring(0,src.absolutePath.length() - 4) + "_cat.jar"

                    File tempFile = new File(temp)
                    if (tempFile.exists()) {
                        tempFile.delete()
                    }

                    JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(tempFile))


                    JarFile jarFile = new JarFile(src)
                    Enumeration<JarEntry> entries = jarFile.entries()
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement()
                        String jarName = jarEntry.name
                        println("jarName==$jarName")


                        InputStream inputStream = jarFile.getInputStream(jarEntry)
                        ZipEntry zipEntry = new ZipEntry(jarName)
                        outputStream.putNextEntry(zipEntry)

                        if (jarName.endsWith(".class") && jarName.startsWith("home")) {
                            ClassReader reader = new ClassReader(inputStream)
                            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
                            ClassVisitor visitor = new CatClassVisitor(writer)
                            reader.accept(visitor, ClassReader.EXPAND_FRAMES)
                            byte[] code = writer.toByteArray()
                            outputStream.write(code)
                        } else {
                            println("unsupported jarName==$jarName")
                            int len = inputStream.read()
                            while (len != -1) {
                                outputStream.write(len)
                                len = inputStream.read()
                            }
                        }
                        inputStream.close()
                    }
                    outputStream.flush()
                    outputStream.close()
                    FileUtils.copyFile(tempFile, dest)
                    tempFile.delete()
//                    tempFile.renameTo(src)
                } else {

                    FileUtils.copyFile(src, dest)
                }

            }
        }
    }
}
