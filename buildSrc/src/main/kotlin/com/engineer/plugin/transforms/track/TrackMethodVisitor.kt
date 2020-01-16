package com.engineer.plugin.transforms.track

import com.engineer.plugin.extensions.model.Constants
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @author rookie
 * @since 01-08-2020
 */
class TrackMethodVisitor(
    api: Int, mv: MethodVisitor?, access: Int, methodName: String?, desc: String?
) : AdviceAdapter(api, mv, access, methodName, desc) {

    private val argumentArrays: Array<Type> = Type.getArgumentTypes(desc)

    override fun onMethodEnter() {
        super.onMethodEnter()


        for (i in argumentArrays.indices) {
            val type = argumentArrays[i]
            val index = i + 1
            mv.visitVarInsn(Opcodes.ALOAD, index)
            box(type)

            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                Constants.tracker,
                "c",
                "(Landroid/view/View;)V",
                false
            )
        }
    }
}