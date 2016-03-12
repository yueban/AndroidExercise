package com.bigfat.md_app_clean.presentation.common.di.components;

import android.content.Context;
import com.bigfat.md_app_clean.data.cache.LoginCache;
import com.bigfat.md_app_clean.data.datastore.LoginDataStore;
import com.bigfat.md_app_clean.domain.di.components.ExecutorComponent;
import com.bigfat.md_app_clean.domain.di.modules.ExecutorModule;
import com.bigfat.md_app_clean.presentation.common.di.modules.ApplicationModule;
import com.bigfat.md_app_clean.presentation.common.navigator.Navigator;
import com.bigfat.md_app_clean.presentation.common.ui.BaseActivity;
import dagger.Component;
import javax.inject.Singleton;
import retrofit2.Retrofit;

/**
 * Created by yueban on 15:08 1/3/16.
 * Email: fbzhh007@gmail.com
 * QQ: 343278606
 */
@Singleton
@Component(modules = { ApplicationModule.class, ExecutorModule.class })
public interface ApplicationComponent extends ExecutorComponent {
    void inject(BaseActivity baseActivity);

    Context context();

    Navigator navigator();

    Retrofit retrofit();

    LoginDataStore loginDataStore();

    LoginCache loginCache();
}
