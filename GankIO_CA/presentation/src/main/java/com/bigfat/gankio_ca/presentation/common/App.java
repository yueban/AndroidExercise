package com.bigfat.gankio_ca.presentation.common;

import android.app.Application;
import com.bigfat.gankio_ca.presentation.common.di.components.ApplicationComponent;
import com.bigfat.gankio_ca.presentation.common.di.components.DaggerApplicationComponent;
import com.bigfat.gankio_ca.presentation.common.di.modules.ApplicationModule;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by yueban on 15:14 24/2/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class App extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        //DBflow初始化
        FlowManager.init(this);
        //Dagger组件初始化
        mApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
