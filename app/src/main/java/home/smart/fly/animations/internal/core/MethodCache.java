package home.smart.fly.animations.internal.core;

import java.util.Vector;


public class MethodCache {

    /**
     * 方法缓存默认大小
     */
    private static final int INIT_CACHE_SIZE = 1024;
    /**
     * 方法名缓存
     */
    private static Vector<MethodInfo> mCacheMethods = new Vector<>(INIT_CACHE_SIZE);

    /**
     * 占位并生成方法ID
     *
     * @return 返回 方法 Id
     */
    public static int request() {
        mCacheMethods.add(new MethodInfo());
        return mCacheMethods.size() - 1;
    }

    public static void addMethodArgument(Object argument, int id) {
        MethodInfo methodInfo = mCacheMethods.get(id);
        methodInfo.addArgument(argument);
    }

    public static void updateMethodInfo(Object result, String className, String methodName, String methodDesc, long startTime, int id) {
        MethodInfo methodInfo = mCacheMethods.get(id);
        methodInfo.setCost((System.currentTimeMillis() - startTime));
        methodInfo.setResult(result);
//        methodInfo.setMethodDesc(methodDesc);
        methodInfo.setClassName(className);
        methodInfo.setMethodName(methodName);
    }

    public static void printMethodInfo(int id) {
        MethodInfo methodInfo = mCacheMethods.get(id);
        Printer.printMethodInfo(methodInfo);
    }
}
