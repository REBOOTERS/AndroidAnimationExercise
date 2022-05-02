package com.example.cpp_native.internal;

public class PatchUtil {
    /**
     * 合并APK文件
     *
     * @param oldApkFile 旧APK文件路径
     * @param newApkFile 新APK文件路径（存储生成的APK的路径）
     * @param patchFile  差异文件
     */
    public native static void patchAPK(String oldApkFile, String newApkFile, String patchFile);
}
