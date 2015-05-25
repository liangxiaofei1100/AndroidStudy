package com.example.alex.common;

import com.gavin.memedia.AppApplication;

/**
 * 方便控制Log开关
 */
public class Log {
    public static final boolean isDebug = true;

    private static final String APP_LOG_TAG = "Alex/";

    public static void v() {
        String caller[] = getCaller();
        v(caller[0], caller[1] + "()");
    }

    public static void v(String message) {
        String caller[] = getCaller();
        v(caller[0], caller[1] + "() " + message);
    }

    public static void v(String tag, String message) {
        if (isDebug) {
            android.util.Log.v(APP_LOG_TAG + tag, message);
        }
    }

    public static void i() {
        String caller[] = getCaller();
        i(caller[0], caller[1] + "()");
    }

    public static void i(String message) {
        String caller[] = getCaller();
        i(caller[0], caller[1] + "() " + message);
    }

    public static void i(String tag, String message) {
        if (isDebug) {
            android.util.Log.i(APP_LOG_TAG + tag, message);
        }
    }

    public static void d() {
        String caller[] = getCaller();
        d(caller[0], caller[1] + "()");
    }

    public static void d(String message) {
        String caller[] = getCaller();
        d(caller[0], caller[1] + "() " + message);
    }

    public static void d(String tag, String message) {
        if (isDebug) {
            android.util.Log.d(APP_LOG_TAG + tag, message);
        }
    }

    public static void w() {
        String caller[] = getCaller();
        w(caller[0], caller[1] + "()");
    }

    public static void w(String message) {
        String caller[] = getCaller();
        w(caller[0], caller[1] + "() " + message);
    }

    public static void w(String tag, String message) {
        if (isDebug) {
            android.util.Log.w(APP_LOG_TAG + tag, message);
        }
    }

    public static void e() {
        String caller[] = getCaller();
        e(caller[0], caller[1] + "()");
    }

    public static void e(String message) {
        String caller[] = getCaller();
        e(caller[0], caller[1] + "() " + message);
    }

    public static void e(String tag, String message) {
        android.util.Log.e(APP_LOG_TAG + tag, message);
    }

    /**
     * 获取调用的类名和方法名。
     *
     * @return String[0]为类名，String[1]为方法名。
     */
    private static String[] getCaller() {
        String caller[] = new String[]{"", ""};
        try {
            StackTraceElement[] traceElements = Thread.currentThread()
                    .getStackTrace();
            String className = traceElements[4].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);

            caller[0] = className;
            caller[1] = traceElements[4].getMethodName();
        } catch (Exception e) {
        }
        return caller;
    }
}
