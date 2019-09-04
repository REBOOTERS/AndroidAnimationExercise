package com.engineer.plugin.transforms.times;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * KoalaLogMethodVisitor.java
 * <p>
 * Created by lijiankun24 on 2018/7/29.
 */
class CatMethodVisitor extends AdviceAdapter {

    // home.smart.fly.animations.internal

    private static final String COST_ANNOTATION_DESC = "Lhome/smart/fly/animations/internal/Cat;";

    private boolean isInjected = false;

    private int startTimeId;

    private int methodId;

    private String className;

    private String methodName;

    private String desc;

    private boolean isStaticMethod;

    private Type[] argumentArrays;

    CatMethodVisitor(int api, MethodVisitor mv, int access, String className, String methodName, String desc) {
        super(api, mv, access, methodName, desc);
        this.className = className;
        this.methodName = methodName;
        this.desc = desc;
        argumentArrays = Type.getArgumentTypes(desc);
        isStaticMethod = ((access & Opcodes.ACC_STATIC) != 0);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        System.out.println("desc "+desc);
        if (COST_ANNOTATION_DESC.equals(desc)) {
            isInjected = true;
        }
        return super.visitAnnotation(desc, visible);
    }

    @Override
    protected void onMethodEnter() {
        if (isInjected) {
            methodId = newLocal(Type.INT_TYPE);
            mv.visitMethodInsn(INVOKESTATIC, "home/smart/fly/animations/internal/core/MethodCache", "request", "()I", false);
            mv.visitIntInsn(ISTORE, methodId);

            for (int i = 0; i < argumentArrays.length; i++) {
                Type type = argumentArrays[i];
                int index = isStaticMethod ? i : (i + 1);
                switch (type.getSort()) {
                    case Type.BOOLEAN:
                    case Type.CHAR:
                    case Type.BYTE:
                    case Type.SHORT:
                    case Type.INT:
                        mv.visitVarInsn(ILOAD, index);
                        box(type);
                        break;
                    case Type.FLOAT:
                        mv.visitVarInsn(FLOAD, index);
                        box(type);
                        break;
                    case Type.LONG:
                        mv.visitVarInsn(LLOAD, index);
                        box(type);
                        break;
                    case Type.DOUBLE:
                        mv.visitVarInsn(DLOAD, index);
                        box(type);
                        break;
                    case Type.ARRAY:
                    case Type.OBJECT:
                        mv.visitVarInsn(ALOAD, index);
                        box(type);
                        break;
                }
                mv.visitVarInsn(ILOAD, methodId);
                visitMethodInsn(INVOKESTATIC, "home/smart/fly/animations/internal/core/MethodCache", "addMethodArgument",
                        "(Ljava/lang/Object;I)V", false);
            }

            startTimeId = newLocal(Type.LONG_TYPE);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitIntInsn(LSTORE, startTimeId);
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        if (isInjected) {
            if (opcode == RETURN) {
                visitInsn(ACONST_NULL);
            } else if (opcode == ARETURN || opcode == ATHROW) {
                dup();
            } else {
                if (opcode == LRETURN || opcode == DRETURN) {
                    dup2();
                } else {
                    dup();
                }
                box(Type.getReturnType(this.methodDesc));
            }
            mv.visitLdcInsn(className);
            mv.visitLdcInsn(methodName);
            mv.visitLdcInsn(desc);
            mv.visitVarInsn(LLOAD, startTimeId);
            mv.visitVarInsn(ILOAD, methodId);
            mv.visitMethodInsn(INVOKESTATIC, "home/smart/fly/animations/internal/core/MethodCache", "updateMethodInfo",
                    "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JI)V", false);

            mv.visitVarInsn(ILOAD, methodId);
            mv.visitMethodInsn(INVOKESTATIC, "home/smart/fly/animations/internal/core/MethodCache",
                    "printMethodInfo", "(I)V", false);
        }
    }
}
