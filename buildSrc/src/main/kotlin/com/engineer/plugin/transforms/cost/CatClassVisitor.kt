package com.engineer.plugin.transforms.cost

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class CatClassVisitor(classVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM6, classVisitor) {

    private lateinit var mClassName: String

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

        methodVisitor =
            CatMethodVisitor(
                Opcodes.ASM6,
                methodVisitor,
                access,
                mClassName,
                name,
                desc
            )
        return methodVisitor
    }
}