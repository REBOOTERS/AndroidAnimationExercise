package com.engineer.plugin.transforms.cat

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class CatClassVisitor(api: Int, private  val catOn: Boolean, classVisitor: ClassVisitor) :
    ClassVisitor(api, classVisitor) {

    private lateinit var mClassName: String

    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        mClassName = name
    }

    override fun visitMethod(
        access: Int, name: String, desc: String?, signature: String?, exceptions: Array<String>?
    ): MethodVisitor {
        var methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)

        if (catOn) {
            methodVisitor = CatMethodVisitor(
                api, methodVisitor, access, mClassName, name, desc
            )
        }

        return methodVisitor
    }
}