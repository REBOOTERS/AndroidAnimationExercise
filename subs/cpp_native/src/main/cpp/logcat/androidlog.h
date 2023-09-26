//
// Created by skye on 2022/4/25.
//

#ifndef MINIAPP_ANDROIDLOG_H
#define MINIAPP_ANDROIDLOG_H

#include <android/log.h>

#define LOG_TAG "imitate_native_log"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGA(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)


#endif //MINIAPP_ANDROIDLOG_H
