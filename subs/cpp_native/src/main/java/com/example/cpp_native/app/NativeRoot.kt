package com.example.cpp_native.app

import android.util.Log
import com.example.cpp_native.internal.NativeHouse

private const val TAG = "NativeRoot"

object NativeRoot {
    private lateinit var nativeHouse: NativeHouse

    fun init() {
        nativeHouse = NativeHouse()
    }

    fun test() {
        lg(nativeHouse.nativeMessage)

        nativeHouse.doHeavyWorkAsync {
            lg("result is $it")
        }
    }

    private fun lg(msg: String) {
        Log.e(TAG, "thread-->${Thread.currentThread().name}: $msg")
    }
}