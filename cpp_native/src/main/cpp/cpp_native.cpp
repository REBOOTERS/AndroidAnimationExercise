#include <jni.h>
#include <string>
#include "androidlog.h"
#include "bspatch.h"

using namespace std;

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_cpp_1native_internal_NativeHouse_getNativeMessage(JNIEnv *env, jobject thiz) {
    LOGE("native method getNativeMessage called");
    string result = "this is from c++";
    return env->NewStringUTF(result.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cpp_1native_internal_PatchUtil_patchAPK(JNIEnv *env, jclass clazz,
                                                         jstring old_apk_file,
                                                         jstring new_apk_file, jstring patch_file) {
    int argc = 4;
    char *argv[argc];
    argv[0] = "bspatch";
    argv[1] = (char *) (env->GetStringUTFChars(old_apk_file, 0));
    argv[2] = (char *) (env->GetStringUTFChars(new_apk_file, 0));
    argv[3] = (char *) (env->GetStringUTFChars(patch_file, 0));

    LOGD("argv[1] = %s", argv[1]);
    LOGD("argv[2] = %s", argv[2]);
    LOGD("argv[3] = %s", argv[3]);

    main(argc, argv);

    env->ReleaseStringUTFChars(old_apk_file, argv[1]);
    env->ReleaseStringUTFChars(new_apk_file, argv[2]);
    env->ReleaseStringUTFChars(patch_file, argv[3]);
}