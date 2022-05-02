#include <jni.h>
#include <string>
#include "logcat/androidlog.h"

using namespace std;

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("cpp_native");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("cpp_native")
//      }
//    }
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_cpp_1native_internal_NativeHouse_getNativeMessage(JNIEnv *env, jobject thiz) {
    LOGE("native method getNativeMessage called");
    string result = "this is from c++";
    return env->NewStringUTF(result.c_str());
}