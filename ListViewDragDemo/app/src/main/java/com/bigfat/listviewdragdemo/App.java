package com.bigfat.listviewdragdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by yueban on 14/7/15.
 */
public class App extends Application {

    private static Context context;
    private static Application app;

    public static Context getContext() {
        return context;
    }

    public static Application getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        app = this;
    }
}
