package com.bigfat.md_app_clean.presentation.common;

import android.app.Application;
import com.bigfat.md_app_clean.data.cache.LoginCache;
import com.bigfat.md_app_clean.data.entity.dbentity.LoginAccountEntity;
import com.bigfat.md_app_clean.presentation.BuildConfig;
import com.bigfat.md_app_clean.presentation.common.di.components.ApplicationComponent;
import com.bigfat.md_app_clean.presentation.common.di.components.DaggerApplicationComponent;
import com.bigfat.md_app_clean.presentation.common.di.components.DaggerUtilComponent;
import com.bigfat.md_app_clean.presentation.common.di.components.UtilComponent;
import com.bigfat.md_app_clean.presentation.common.di.modules.ApplicationModule;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;
import rx.functions.Action1;

/**
 * Created by yueban on 15:07 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
public class App extends Application {
    private ApplicationComponent mApplicationComponent;
    private UtilComponent mUtilComponent;
    private LoginAccountEntity mLoginAccountEntity;

    @Override
    public void onCreate() {
        super.onCreate();

        //DBflow初始化
        FlowManager.init(this);

        //Application组件初始化
        mApplicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .build();

        //Util组件初始化
        mUtilComponent = DaggerUtilComponent.builder()
            .applicationComponent(mApplicationComponent)
            .build();

        //登录信息刷新订阅
        LoginCache mLoginCache = mApplicationComponent.loginCache();
        mLoginCache.setLoginAccountEntityAction1(new LoginAccountAction());

        if (BuildConfig.DEBUG) {
            //stetho初始化
            Stetho.initializeWithDefaults(this);
        }
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public UtilComponent getUtilComponent() {
        return mUtilComponent;
    }

    public LoginAccountEntity getLoginAccountEntity() {
        return mLoginAccountEntity;
    }

    class LoginAccountAction implements Action1<LoginAccountEntity> {

        @Override
        public void call(LoginAccountEntity loginAccountEntity) {
            mLoginAccountEntity = loginAccountEntity;
        }
    }
}
