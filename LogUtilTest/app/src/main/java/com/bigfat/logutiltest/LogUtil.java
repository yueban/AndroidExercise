package com.bigfat.logutiltest;

import android.util.Log;

/**
 * Created by yueban on 15/3/30.
 */
public class LogUtil {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            Log.i(tag, msg, tr);
        }
    }
}
