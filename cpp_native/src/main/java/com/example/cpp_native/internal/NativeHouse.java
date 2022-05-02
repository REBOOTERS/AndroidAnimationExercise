package com.example.cpp_native.internal;

public class NativeHouse {

    static {
        System.loadLibrary("cpp_native");
    }

    public native String getNativeMessage();
}
