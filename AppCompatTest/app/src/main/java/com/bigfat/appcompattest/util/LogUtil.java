package com.bigfat.appcompattest.util;

import android.util.Log;

import com.bigfat.appcompattest.BuildConfig;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/3/16
 */
public class LogUtil {

    private static final boolean isDebug = BuildConfig.DEBUG;
    private static String classname;

    static {
        classname = LogUtil.class.getName();
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            //new Throwable() 可以获得运行到某一行代码时涉及到的 类、函数
            for (StackTraceElement st : (new Throwable()).getStackTrace()) {
                if (!classname.equals(st.getClassName())) {
                    int b = st.getClassName().lastIndexOf(".") + 1;
                    Log.i(st.getClassName().substring(b), msg);
                    return;
                }
            }
        }
    }
}
