#include <jni.h>
#include <string>
#include "androidlog.h"
#include "bspatch.h"
#include <thread>

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
    argv[0] = (char *) ("bspatch");
    argv[1] = (char *) (env->GetStringUTFChars(old_apk_file, nullptr));
    argv[2] = (char *) (env->GetStringUTFChars(new_apk_file, nullptr));
    argv[3] = (char *) (env->GetStringUTFChars(patch_file, nullptr));

    LOGD("argv[1] = %s", argv[1]);
    LOGD("argv[2] = %s", argv[2]);
    LOGD("argv[3] = %s", argv[3]);

    main(argc, argv);

    env->ReleaseStringUTFChars(old_apk_file, argv[1]);
    env->ReleaseStringUTFChars(new_apk_file, argv[2]);
    env->ReleaseStringUTFChars(patch_file, argv[3]);
}

static JavaVM* gVm = nullptr;   // 全局保存，供子线程 attach

// so 被加载时会把 JavaVM 存下来
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*) {
    gVm = vm;
    return JNI_VERSION_1_6;
}

// 子线程里真正干活的函数
void worker(JNIEnv* env, jobject gCallback) {
    // 1. 模拟耗时 2 秒
    std::this_thread::sleep_for(std::chrono::seconds(2));

    // 2. 找到接口类
    jclass clazz = env->GetObjectClass(gCallback);
    jmethodID onResult = env->GetMethodID(clazz,
                                          "onResult",
                                          "(Ljava/lang/String;)V");

    // 3. 拼结果
    jstring msg = env->NewStringUTF("C++ 线程执行完毕");

    // 4. 回调 Java
    env->CallVoidMethod(gCallback, onResult, msg);

    // 5. 清理局部引用
    env->DeleteLocalRef(msg);
    env->DeleteLocalRef(clazz);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_cpp_1native_internal_NativeHouse_doHeavyWorkAsync(JNIEnv *env, jobject thiz,
                                                                   jobject callback) {
    // 1. callback 是局部变量，跨线程必须提升为全局引用
    jobject gCallback = env->NewGlobalRef(callback);
    if (gCallback == nullptr) return;
    // 2. 起 C++ 线程
    std::thread([gCallback]() {
        JNIEnv* env = nullptr;
        // 3. 子线程 attach
        if (gVm->AttachCurrentThread(&env, nullptr) != 0) return;
        worker(env, gCallback);   // 真正干活
        // 4. 别忘了 detach，否则线程退出会崩
        gVm->DetachCurrentThread();
        // 5. 全局引用用完要释放
        env->DeleteGlobalRef(gCallback);
    }).detach();
}

