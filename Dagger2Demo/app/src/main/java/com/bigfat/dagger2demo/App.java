package com.bigfat.dagger2demo;

import com.bigfat.dagger2demo.app.ApplicationComponent;
import com.bigfat.dagger2demo.app.ApplicationModule;
import com.bigfat.dagger2demo.app.DaggerApplicationComponent;

/**
 * Created by yueban on 14:07 19/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class App extends android.app.Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent =
            DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
