package com.yueban.accessibilitydemo;

import android.app.Application;
import android.content.Context;

import com.elvishew.xlog.XLog;

/**
 * @author yueban fbzhh007@gmail.com
 * @date 2019-12-16
 */
public class MyApp extends Application {
    private static Context sBaseContext;

    public static Context getContext() {
        return sBaseContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sBaseContext = this;

        XLog.init();
    }
}
