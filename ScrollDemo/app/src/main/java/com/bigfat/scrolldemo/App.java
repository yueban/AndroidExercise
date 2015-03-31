package com.bigfat.scrolldemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by yueban on 15/3/31.
 */
public class App extends Application{
    private static App application;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context =getApplicationContext();
    }

    public static App getInstance(){
        if(application == null){
            application = new App();
        }
        return application;
    }

    public static Context getContext(){
        return context;
    }
}
