package com.engineer.plugin.transforms.cat

import com.engineer.plugin.extensions.PhoenixExtension
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class CatClassVisitor(private val project: Project, classVisitor: ClassVisitor) :
    ClassVisitor(Opcodes.ASM6, classVisitor) {

    private lateinit var mClassName: String
    private var catOn = true

    init {
        val phoneix = project.extensions.findByType(PhoenixExtension::class.java)
        val transform = phoneix?.transform
        catOn = transform?.catOn ?: true
    }

    override fun visit(
        version: Int, access: Int, name: String,
        signature: String?, superName: String?, interfaces: Array<String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        mClassName = name
    }

    override fun visitMethod(
        access: Int, name: String, desc: String?,
        signature: String?, exceptions: Array<String>?
    ): MethodVisitor {
        var methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)

        if (catOn) {
            methodVisitor =
                CatMethodVisitor(
                    Opcodes.ASM6,
                    methodVisitor,
                    access,
                    mClassName,
                    name,
                    desc
                )
        }

        return methodVisitor
    }
}