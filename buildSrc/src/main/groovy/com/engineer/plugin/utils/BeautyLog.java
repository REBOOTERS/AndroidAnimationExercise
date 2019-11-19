package com.engineer.plugin.utils;

import org.gradle.api.logging.Logger;

/**
 * @author zhuyongging @ Zhihu Inc.
 * @since 11-19-2019
 */
public class BeautyLog {
    public static void log(String tag, boolean isStart, Logger logger) {
        if (isStart) {
            System.out.println();
            String msg =
                    "=================================== " + tag + " ===================================>>>>";
            logger.error(msg);
            System.out.println();
        } else {
            System.out.println();
            String msg =
                    "<<<<=================================== " + tag + " ===================================";
            logger.error(msg);
            System.out.println();
        }
    }
}
