package com.yueban.accessibilitydemo.util;

import com.elvishew.xlog.XLog;

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2019-12-16
 */
public class LogUtils {
    public static void d(String msg) {
        XLog.d(msg);
    }

    public static void d(String tag, String msg) {
        XLog.tag(tag).d(msg);
    }
}
