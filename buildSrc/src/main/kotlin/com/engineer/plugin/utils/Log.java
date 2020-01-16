package com.engineer.plugin.utils;

import com.asm.Music;

public class Log {
    public static void d(String tag, String msg) {
        System.out.println(tag + " ==> " + msg);
    }

    public static void x(Music musci) {
        System.out.println("music " + musci);
    }
}
