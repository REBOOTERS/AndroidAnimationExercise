package com.engineer.ai

import org.pytorch.Tensor

class Mina {
    fun kk() {
        val zDim: IntArray = intArrayOf(1, 100)
        val outDims: IntArray = intArrayOf(64, 64, 3)

        val z = FloatArray(zDim[0] * outDims[1])

        val shape = longArrayOf(1, 100)

        val tensor = Tensor.fromBlob(z, shape)
    }
}
