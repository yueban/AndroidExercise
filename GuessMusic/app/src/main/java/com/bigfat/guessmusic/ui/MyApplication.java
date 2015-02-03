package com.bigfat.guessmusic.ui;

import android.app.Application;
import android.content.Context;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2015/2/3
 */
public class MyApplication extends Application {
    private static Context context;

    public MyApplication() {
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
