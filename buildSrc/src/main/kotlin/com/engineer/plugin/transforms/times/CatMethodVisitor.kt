package com.engineer.plugin.transforms.times

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

internal class CatMethodVisitor(
    api: Int, mv: MethodVisitor?, access: Int,
    private val className: String?,
    private val methodName: String?,
    private val desc: String?
) : AdviceAdapter(api, mv, access, methodName, desc) {
    private var isInjected = false
    private var startTimeId = 0
    private var methodId = 0
    private val isStaticMethod: Boolean
    private val argumentArrays: Array<Type>


    override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor {
        //  System.out.println("desc "+desc);
        if (COST_ANNOTATION_DESC == desc) {
            isInjected = true
        }
        return super.visitAnnotation(desc, visible)
    }

    override fun onMethodEnter() {
        if (isInjected) {
            methodId = newLocal(Type.INT_TYPE)
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "home/smart/fly/animations/internal/core/MethodCache",
                "request",
                "()I",
                false
            )
            mv.visitIntInsn(Opcodes.ISTORE, methodId)
            for (i in argumentArrays.indices) {
                val type = argumentArrays[i]
                val index = if (isStaticMethod) i else i + 1
                when (type.sort) {
                    Type.BOOLEAN, Type.CHAR, Type.BYTE, Type.SHORT, Type.INT -> {
                        mv.visitVarInsn(Opcodes.ILOAD, index)
                        box(type)
                    }
                    Type.FLOAT -> {
                        mv.visitVarInsn(Opcodes.FLOAD, index)
                        box(type)
                    }
                    Type.LONG -> {
                        mv.visitVarInsn(Opcodes.LLOAD, index)
                        box(type)
                    }
                    Type.DOUBLE -> {
                        mv.visitVarInsn(Opcodes.DLOAD, index)
                        box(type)
                    }
                    Type.ARRAY, Type.OBJECT -> {
                        mv.visitVarInsn(Opcodes.ALOAD, index)
                        box(type)
                    }
                }
                mv.visitVarInsn(Opcodes.ILOAD, methodId)
                visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "home/smart/fly/animations/internal/core/MethodCache",
                    "addMethodArgument",
                    "(Ljava/lang/Object;I)V",
                    false
                )
            }
            startTimeId = newLocal(Type.LONG_TYPE)
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "java/lang/System",
                "currentTimeMillis",
                "()J",
                false
            )
            mv.visitIntInsn(Opcodes.LSTORE, startTimeId)
        }
    }

    override fun onMethodExit(opcode: Int) {
        if (isInjected) {
            if (opcode == Opcodes.RETURN) {
                visitInsn(Opcodes.ACONST_NULL)
            } else if (opcode == Opcodes.ARETURN || opcode == Opcodes.ATHROW) {
                dup()
            } else {
                if (opcode == Opcodes.LRETURN || opcode == Opcodes.DRETURN) {
                    dup2()
                } else {
                    dup()
                }
                box(Type.getReturnType(methodDesc))
            }
            mv.visitLdcInsn(className)
            mv.visitLdcInsn(methodName)
            mv.visitLdcInsn(desc)
            mv.visitVarInsn(Opcodes.LLOAD, startTimeId)
            mv.visitVarInsn(Opcodes.ILOAD, methodId)
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "home/smart/fly/animations/internal/core/MethodCache",
                "updateMethodInfo",
                "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JI)V",
                false
            )
            mv.visitVarInsn(Opcodes.ILOAD, methodId)
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "home/smart/fly/animations/internal/core/MethodCache",
                "printMethodInfo",
                "(I)V",
                false
            )
        }
    }

    companion object {
        // home.smart.fly.animations.internal
        private const val COST_ANNOTATION_DESC =
            "Lhome/smart/fly/animations/internal/annotations/Cat;"
    }

    init {
        argumentArrays = Type.getArgumentTypes(desc)
        isStaticMethod = access and Opcodes.ACC_STATIC != 0
    }
}