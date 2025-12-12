package com.example.cpp_native.internal;

public class NativeHouse {

    static {
        System.loadLibrary("cpp_native");
    }

    public native String getNativeMessage();

    // 3. 声明 native：把耗时任务丢给 C++，并传一个 callback 实例
    public native void doHeavyWorkAsync(ResultCallback cb);


    public interface ResultCallback {
        void onResult(String msg);   // 也可以带 int、byte[] 等多参数
    }
}
